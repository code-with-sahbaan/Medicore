import { Component, ElementRef, ViewChild } from '@angular/core';
import { WebRtcService } from '../../service/webRtcService.service';
import { CalendarService } from '../../service/calendar.service';
import { Button, ButtonModule } from "primeng/button";
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-appointment-conversation',
  imports: [ButtonModule, NgIf],
  templateUrl: './appointment-conversation.component.html',
  styleUrl: './appointment-conversation.component.css'
})
export class AppointmentConversationComponent {
  @ViewChild('localVideo', { static: true }) localRef!: ElementRef<HTMLVideoElement>;
  @ViewChild('remoteVideo', { static: true }) remoteRef!: ElementRef<HTMLVideoElement>;

  isCallJoined: boolean = false;

  videoText: string = 'Stop Video';
  audioText: string = 'Mute';

  constructor(private webRtc: WebRtcService, private calendarService: CalendarService) { }

  ngOnDestroy() {
    this.webRtc.hangup();
  }

  async ngOnInit() {
    if (!this.calendarService.roomId) {
      history.back();
    }
    const roomId = this.calendarService.roomId;

    await this.webRtc.init(roomId, (remoteStream) => {
      this.remoteRef.nativeElement.srcObject = remoteStream;
    });

    const local = this.webRtc.getLocalStream();
    this.localRef.nativeElement.srcObject = local;
  }

  async startCall() {
    await this.webRtc.call();
    this.isCallJoined = true;
  }

  async updateVideo() {
    this.webRtc.isVideo = !this.webRtc.isVideo;
    if (this.webRtc.isVideo) {
      this.videoText = 'Stop Video';
    } else {
      this.videoText = 'Start Video';
    }
    const videoTrack = this.webRtc.localStream.getVideoTracks()[0];
    videoTrack.enabled = this.webRtc.isVideo;
  }

  updateAudio() {
    this.webRtc.isAudio = !this.webRtc.isAudio;
    if (this.webRtc.isAudio) {
      this.audioText = 'Mute';
    } else {
      this.audioText = 'Un-Mute'
    }
    const audioTrack = this.webRtc.localStream.getAudioTracks()[0];
    audioTrack.enabled = !audioTrack.enabled;
  }

  hangup() {
    this.webRtc.hangup();
    history.back();
  }
}
