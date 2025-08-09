import { Component, ElementRef, ViewChild } from '@angular/core';
import { WebRtcService } from '../../service/webRtcService.service';

@Component({
  selector: 'app-appointment-conversation',
  imports: [],
  templateUrl: './appointment-conversation.component.html',
  styleUrl: './appointment-conversation.component.css'
})
export class AppointmentConversationComponent {
  @ViewChild('localVideo', { static: true }) localRef!: ElementRef<HTMLVideoElement>;
  @ViewChild('remoteVideo', { static: true }) remoteRef!: ElementRef<HTMLVideoElement>;

  constructor(private webRtc: WebRtcService) { }

  async ngOnInit() {
    const token = '<JWT_FROM_LOGIN>'; // Get from your AuthService
    const roomId = 'room123';

    await this.webRtc.init(token, roomId, (remoteStream) => {
      this.remoteRef.nativeElement.srcObject = remoteStream;
    });

    const local = this.webRtc.getLocalStream();
    this.localRef.nativeElement.srcObject = local;
  }

  async startCall() {
    await this.webRtc.call();
  }

  hangup() {
    this.webRtc.hangup();
  }
}
