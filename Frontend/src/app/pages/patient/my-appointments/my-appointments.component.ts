import { Component } from '@angular/core';
import { CalendarComponent } from '../../../components/patient/calendar/calendar.component';

@Component({
  selector: 'app-my-appointments',
  imports: [CalendarComponent],
  templateUrl: './my-appointments.component.html',
  styleUrl: './my-appointments.component.css'
})
export class MyAppointmentsComponent {

}
