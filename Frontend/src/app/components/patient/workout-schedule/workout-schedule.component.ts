import { Component } from '@angular/core';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import timelinePlugin from '@fullcalendar/timeline';

@Component({
  selector: 'app-workout-schedule',
  imports: [FullCalendarModule],
  templateUrl: './workout-schedule.component.html',
  styleUrl: './workout-schedule.component.css'
})
export class WorkoutScheduleComponent {
  calendarOptions: CalendarOptions = {
    plugins: [timelinePlugin],
    initialView: 'timelineDay', // 👈 horizontal timeline for today
    slotDuration: '01:00:00',   // 1-hour slots
    headerToolbar: false,
    slotMinWidth:150,
    events: [
      {
        title: 'Swimming',
        start: new Date().setHours(9, 0),
        end: new Date().setHours(10, 0)
      },
      {
        title: 'Cycling',
        start: new Date().setHours(9, 30),
        end: new Date().setHours(12, 0)
      }
    ],
    height: 'auto',
    nowIndicator: true
  };
}
