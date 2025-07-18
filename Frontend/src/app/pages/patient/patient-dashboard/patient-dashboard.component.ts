import { Component } from '@angular/core';
import { TabsModule } from 'primeng/tabs';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { AccordionModule } from 'primeng/accordion';
import { WorkoutScheduleComponent } from '../../../components/patient/workout-schedule/workout-schedule.component';
import { ButtonModule } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { Listbox } from 'primeng/listbox';
import { DashboardService } from '../../../service/patient/dashboard.service';
import { FormsModule } from '@angular/forms';
import { DatePicker } from 'primeng/datepicker';

@Component({
  selector: 'app-patient-dashboard',
  imports: [CommonModule, TabsModule, CardModule, TableModule, AccordionModule, WorkoutScheduleComponent, ButtonModule, Dialog, InputTextModule, Listbox, FormsModule, DatePicker],
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent {

  constructor(public dashboardService: DashboardService) { }
  workoutEvent = {
    title: '',
    start: new Date(),
    end: new Date()
  }

  visible: boolean = false;
  appointments = [
    {
      appointmentId: 19654,
      doctorName: 'John Doe',
      appointmentDate: '12-Aug-2025',
      appointmentTime: '11:05 AM',
    }
  ];

  showDialog() {
    this.visible = true;
  }

  addWorkout() {

    // Start Time Extraction
    const startHours = this.workoutEvent.start.getHours();
    const startMinutes = this.workoutEvent.start.getMinutes();

    // End Time Extraction
    const endHours = this.workoutEvent.end.getHours();
    const endMinutes = this.workoutEvent.end.getMinutes();

    // Preparing Object
    const finalEvent = {
      title: this.workoutEvent.title,
      start: new Date().setHours(startHours, startMinutes),
      end: new Date().setHours(endHours, endMinutes)
    }
    this.dashboardService.workoutSchedules = [...this.dashboardService.workoutSchedules, finalEvent];
    this.dashboardService.calendarOptions = {
      ...this.dashboardService.calendarOptions,
      events: [...this.dashboardService.workoutSchedules] // create new array reference
    };
    this.visible = false
  }
}
