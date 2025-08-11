import { Injectable } from '@angular/core';
import { getJWTtoken } from '../core/auth.utils';


@Injectable({ providedIn: 'root' })
export class WebRtcService {

    private pc!: RTCPeerConnection;
    public localStream!: MediaStream;
    private ws!: WebSocket;
    private roomId!: string;
    private pendingCandidates: RTCIceCandidateInit[] = [];
    public isVideo: boolean = true;
    public isAudio: boolean = true;
    public prescription: string = "";

    // Replace with your TURN/STUN servers
    private rtcConfig: RTCConfiguration = {
        iceServers: [
            { urls: 'stun:stun.l.google.com:19302' },
            // { urls: 'turn:turn.example.com:3478', username: 'user', credential: 'pass' }
        ]
    };

    async init(roomId: string, onRemoteStream: (s: MediaStream) => void) {
        this.roomId = roomId;
        this.pc = new RTCPeerConnection(this.rtcConfig);

        // handle remote tracks
        this.pc.ontrack = (event) => {
            onRemoteStream(event.streams[0]);
        };

        // send ICE candidates to signaling server
        this.pc.onicecandidate = (evt) => {
            if (evt.candidate) {
                this.send({ type: 'ice', rid: roomId, candidate: evt.candidate });
            }
        };

        // get local media
        this.localStream = await navigator.mediaDevices.getUserMedia({ audio: this.isAudio, video: this.isVideo });
        // add tracks
        this.localStream.getTracks().forEach(track => this.pc.addTrack(track, this.localStream));

        // connect signaling WebSocket (token as query param)
        this.ws = new WebSocket(`http://localhost:5000/ws/signal?token=Bearer ${getJWTtoken()}`);
        this.ws.onopen = () => this.send({ type: 'join', rid: roomId });
        this.ws.onmessage = async (msg) => {
            const data = JSON.parse(msg.data);
            switch (data.type) {
                case 'offer':
                    await this.pc.setRemoteDescription({ type: 'offer', sdp: data.sdp });
                    const answer = await this.pc.createAnswer();
                    await this.pc.setLocalDescription(answer);
                    this.send({ type: 'answer', rid: roomId, sdp: answer.sdp });

                    // process buffered candidates
                    this.pendingCandidates.forEach(c => this.pc.addIceCandidate(c));
                    this.pendingCandidates = [];
                    break;

                case 'answer':
                    await this.pc.setRemoteDescription({ type: 'answer', sdp: data.sdp });

                    // process buffered candidates
                    this.pendingCandidates.forEach(c => this.pc.addIceCandidate(c));
                    this.pendingCandidates = [];
                    break;

                case 'ice':
                    if (data.candidate) {
                        if (this.pc.remoteDescription) {
                            await this.pc.addIceCandidate(data.candidate);
                        } else {
                            this.pendingCandidates.push(data.candidate);
                        }
                    }
                    break;
                case 'prescription':
                    this.prescription = data.message;    
            }
        };
    }

    public send(obj: any) {
        if (this.ws?.readyState === WebSocket.OPEN) {
            this.ws.send(JSON.stringify(obj));
        }
    }

    async call() {
        // create offer and send to other peer
        const offer = await this.pc.createOffer();
        await this.pc.setLocalDescription(offer);
        const rid = this.roomId;
        this.send({ type: 'offer', rid, sdp: offer.sdp });
    }

    getLocalStream() { return this.localStream; }

    hangup() {
        const rid = this.roomId;
        this.send({ type: 'leave', rid });
        this.pc?.close();
        this.localStream?.getTracks().forEach(t => t.stop());
        this.ws?.close();
    }
}
