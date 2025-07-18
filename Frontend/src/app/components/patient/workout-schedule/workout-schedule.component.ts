import { Component, OnInit } from '@angular/core';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import { DashboardService } from '../../../service/patient/dashboard.service';

@Component({
  selector: 'app-workout-schedule',
  imports: [FullCalendarModule],
  templateUrl: './workout-schedule.component.html',
  styleUrl: './workout-schedule.component.css'
})
export class WorkoutScheduleComponent implements OnInit {

  public calendarOptions: CalendarOptions | undefined;

  constructor(public dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.calendarOptions = this.dashboardService.calendarOptions;
  }


}
