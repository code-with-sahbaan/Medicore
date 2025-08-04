import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarOptions } from '@fullcalendar/core';
import { CalendarService } from '../../../service/calendar.service';
import { UiService } from '../../../service/ui.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-calendar',
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css'
})
export class CalendarComponent implements OnInit {

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
      }
    }
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
