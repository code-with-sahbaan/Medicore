import { Component, OnInit } from '@angular/core';
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
import { FieldsetModule } from 'primeng/fieldset';
import { UiService } from '../../../service/ui.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-patient-dashboard',
  imports: [CommonModule, TabsModule, CardModule, TableModule, AccordionModule, WorkoutScheduleComponent, ButtonModule, Dialog, InputTextModule, Listbox, FormsModule, DatePicker, FieldsetModule],
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent implements OnInit {

  constructor(public dashboardService: DashboardService, public uiService: UiService) { }

  ngOnInit(): void {
    setTimeout(() => this.getDashboardData(), 0);
  }

  workoutEvent = {
    title: '',
    start: new Date(),
    end: new Date()
  }

  visible: boolean = false;
  
  patientAppointments = [
    {
      appointmentId: 19654,
      doctorName: 'John Doe',
      appointmentDate: '12-Aug-2025',
      appointmentTime: '11:05 AM',
    }
  ];

  credits: number = 300;
  totalAppointments: number = 20;

  showDialog() {
    this.visible = true;
  }

  formatDate(dateInput: Date): string {
    const date = new Date(dateInput);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
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

    // Adding the object into arrays
    this.dashboardService.workoutSchedules = [...this.dashboardService.workoutSchedules, finalEvent];
    this.dashboardService.calendarOptions.events = this.dashboardService.workoutSchedules;

    // Disposing the Dialog
    this.workoutEvent = {
      title: '',
      start: new Date(),
      end: new Date()
    };
    this.visible = false
  }

  convertTo12Hour(time24: string): string {
    const [hourStr, minute] = time24.split(':');
    let hour = parseInt(hourStr, 10);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    hour = hour % 12 || 12;
    return `${hour}:${minute} ${ampm}`;
  }

  getDashboardData() {
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    this.dashboardService
      .getDashboardData()
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
          this.credits = data.credits;
          this.totalAppointments = data.totalAppointments;
          this.patientAppointments = data.patientAppointments;
          this.dashboardService.workoutSchedules = data.patientWorkouts;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
