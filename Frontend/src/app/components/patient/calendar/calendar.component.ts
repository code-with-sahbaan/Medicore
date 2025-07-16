import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarOptions } from '@fullcalendar/core';

@Component({
  selector: 'app-calendar',
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css'
})
export class CalendarComponent {
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    headerToolbar: {
      // left: 'prev,next today',
      // center: 'title',
      right: 'timeGridWeek',
      // right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    events: [
      { title: 'Appointment', date: '2025-07-20' },
      { title: 'Consultation', date: '2025-07-21' }
    ],
    selectable: true,
    editable: true,
  };
}
