import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarOptions, EventClickArg, EventSourceInput } from '@fullcalendar/core';
import { CalendarService, Event } from '../../../service/calendar.service';
import { UiService } from '../../../service/ui.service';
import { finalize } from 'rxjs';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Timeline, TimelineModule } from 'primeng/timeline';

@Component({
  selector: 'app-calendar',
  imports: [CommonModule, FullCalendarModule, DialogModule, FormsModule, ReactiveFormsModule, ButtonModule, Timeline],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css'
})
export class CalendarComponent implements OnInit {

  visible: boolean = false;

  timeline: any[] = [];

  joinConversation : boolean = true;

  event: Event = {
    title: '',
    start: new Date(),
    end: new Date(),
    allDay: false
  }

  calendarOptions: CalendarOptions = {}

  ngOnInit(): void {
    setTimeout(() => this.getAllAppointments(), 0);
  }

  constructor(public calendarService: CalendarService, public uiService: UiService) {
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay',
      },
      events: [...this.calendarService.events],
      eventTimeFormat: {
        hour: 'numeric',
        minute: '2-digit',
        second: undefined,
        meridiem: true
      },
      eventClick: this.viewEventDetail.bind(this)
    }
  }

  viewEventDetail(arg: EventClickArg) {
    const fetchedEvent = arg.event;
    this.event = {
      title: fetchedEvent.title,
      start: fetchedEvent.start ?? new Date(),
      end: fetchedEvent.end ?? new Date(),
      allDay: fetchedEvent.allDay,
    }

    const extraProps = fetchedEvent.extendedProps;
    this.joinConversation = !(extraProps['isAppointmentTimeOccurred']);
    this.timeline = [
      { date: this.event.start, state: "Appointment Start" },
      { date: this.event.end, state: "Appointment End" }
    ]
    this.visible = true;
  }

  cancelAppointment(data: Event){
    if(confirm('Are you sure you wanna cancel appointment')){
      this.visible = false;
    }else{
      this.visible = true;
    }
  }

  formatDate(dateInput: Date): string {
    const date = new Date(dateInput);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${date.getDay()}-${date.getMonth()}-${date.getFullYear()} ${hours}:${minutes}`;
  }

  getAllAppointments() {
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    this.calendarService
      .getAllAppointments()
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const data = response.responseBody;
          this.calendarOptions.events = [...data]
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

}
