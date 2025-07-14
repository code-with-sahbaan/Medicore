import { Component } from '@angular/core';
import { TabsModule } from 'primeng/tabs';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-patient-dashboard',
  imports: [CommonModule, TabsModule, CardModule, TableModule],
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent {
  appointments: [{},{}] = [
    {
      appointmentId: 19654,
      doctorName: 'John Doe',
      appointmentDate: '12-Aug-2025',
      appointmentTime: '11:05 AM',
    },
    {
      appointmentId: 19655,
      doctorName: 'John Doe 02',
      appointmentDate: '13-Aug-2025',
      appointmentTime: '11:05 AM',
    },
  ];
}
