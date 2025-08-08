import { Injectable } from '@angular/core';


@Injectable({ providedIn: 'root' })
export class WebRtcService {

    private pc!: RTCPeerConnection;
    private localStream!: MediaStream;
    private ws!: WebSocket;
    private roomId!: string;

    // Replace with your TURN/STUN servers
    private rtcConfig: RTCConfiguration = {
        iceServers: [
            { urls: 'stun:stun.l.google.com:19302' },
            // { urls: 'turn:turn.example.com:3478', username: 'user', credential: 'pass' }
        ]
    };

    async init(token: string, roomId: string, onRemoteStream: (s: MediaStream) => void) {
        this.roomId = roomId;
        this.pc = new RTCPeerConnection(this.rtcConfig);

        // handle remote tracks
        this.pc.ontrack = (event) => {
            onRemoteStream(event.streams[0]);
        };

        // send ICE candidates to signaling server
        this.pc.onicecandidate = (evt) => {
            if (evt.candidate) {
                this.send({ type: 'ice', roomId, candidate: evt.candidate });
            }
        };

        // get local media
        this.localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
        // add tracks
        this.localStream.getTracks().forEach(track => this.pc.addTrack(track, this.localStream));

        // connect signaling WebSocket (token as query param)
        this.ws = new WebSocket(`wss://your-server.example/ws/signal?token=${token}`);
        this.ws.onopen = () => this.send({ type: 'join', roomId });
        this.ws.onmessage = async (msg) => {
            const data = JSON.parse(msg.data);
            switch (data.type) {
                case 'offer':
                    await this.pc.setRemoteDescription(new RTCSessionDescription({ type: 'offer', sdp: data.sdp }));
                    const answer = await this.pc.createAnswer();
                    await this.pc.setLocalDescription(answer);
                    this.send({ type: 'answer', roomId, sdp: answer.sdp });
                    break;
                case 'answer':
                    await this.pc.setRemoteDescription(new RTCSessionDescription({ type: 'answer', sdp: data.sdp }));
                    break;
                case 'ice':
                    if (data.candidate) {
                        try {
                            await this.pc.addIceCandidate(data.candidate);
                        } catch (e) { console.warn(e); }
                    }
                    break;
            }
        };
    }

    private send(obj: any) {
        if (this.ws?.readyState === WebSocket.OPEN) this.ws.send(JSON.stringify(obj));
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
