import { Component, ElementRef, ViewChild } from '@angular/core';
import { WebRtcService } from '../../service/webRtcService.service';
import { CalendarService } from '../../service/calendar.service';
import { Button, ButtonModule } from "primeng/button";

@Component({
  selector: 'app-appointment-conversation',
  imports: [ButtonModule],
  templateUrl: './appointment-conversation.component.html',
  styleUrl: './appointment-conversation.component.css'
})
export class AppointmentConversationComponent {
  @ViewChild('localVideo', { static: true }) localRef!: ElementRef<HTMLVideoElement>;
  @ViewChild('remoteVideo', { static: true }) remoteRef!: ElementRef<HTMLVideoElement>;

  isCallJoined: boolean = false;

  constructor(private webRtc: WebRtcService, private calendarService: CalendarService) { }

  async ngOnInit() {
    if(!this.calendarService.roomId) {
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

  hangup() {
    this.webRtc.hangup();
    history.back();
  }
}
