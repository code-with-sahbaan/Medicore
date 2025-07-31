import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SortEvent } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { BookAppointment, BookAppointmentService, GetSlots, Pageable } from '../../../service/patient/bookAppointment.service';
import { finalize } from 'rxjs';
import { UiService } from '../../../service/ui.service';
import { Dialog } from 'primeng/dialog';
import { DatePicker } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';
import { SelectButtonModule } from 'primeng/selectbutton';

@Component({
  selector: 'app-book-appointment',
  imports: [InputTextModule, TableModule, CommonModule, ButtonModule, Dialog, DatePicker, FormsModule, SelectButtonModule],
  templateUrl: './book-appointment.component.html',
  styleUrl: './book-appointment.component.css'
})
export class BookAppointmentComponent {

  constructor(public bookAppointmentService: BookAppointmentService, public uiService: UiService) { }

  consultants: any[] = [];

  // pageNumber
  first: number = 0;
  // totalRecords
  rows: number = 10;
  // SortDirection
  order: number = 1;
  // SortByProperty
  sort: string = "fullName";
  // pageSize
  size: number = 10;

  isSorted: boolean = false;

  visible: boolean = false;

  appointmentDate: Date = new Date();

  minDate: Date = this.getCurrentTimeZoneDate(new Date());

  availableTimes: { label: string; value: string }[] = [];

  appointmentTime: { label: string; value: string }[] = [];

  doctorEmail: string = "";

  selectedConsultant: any = {};

  loadAvailableTimes() {
    this.getAllAvailableSlots(this.appointmentDate);
    this.appointmentTime = [];
  }

  showDialog(consultant: any) {
    this.visible = true;
    this.selectedConsultant = consultant;
    this.doctorEmail = consultant.email;
    this.loadAvailableTimes();
  }

  bookAppointment() {
    const payload: BookAppointment = {
      doctorEmail: this.doctorEmail,
      appointmentDate: this.appointmentDate,
      appointmentTimes: this.appointmentTime
    }
    this.uiService.showSpinner();
    this.bookAppointmentService
      .bookAppointment(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: () => {
          // Showing success Toast
          this.uiService.showSuccess("Appointment(s) Booked Successfully!");
          this.visible = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  pageable: Pageable = {
    page: this.first,
    size: this.size,
    order: this.order,
    sort: this.sort
  }

  getAllActiveConsultants(event: any) {

    this.pageable = {
      page: event.first,
      size: event.rows ?? 10,
      sort: event.sortField ?? this.sort,
      order: event.sortOrder ?? this.order
    }
    this.uiService.showSpinner();
    this.bookAppointmentService
      .getAllConsultants(this.pageable)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response: any) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const data = response.responseBody;
          this.consultants = [...data.content];
          const page = data.page;
          this.rows = page.totalElements;
          this.first = page.number;
          this.size = page.size;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  getCurrentTimeZoneDate(date: Date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes() - date.getTimezoneOffset());
  }

  getAllAvailableSlots(date: Date) {

    const getSlot: GetSlots = {
      doctorEmail: this.doctorEmail,
      appointmentDate: this.getCurrentTimeZoneDate(date)
    }
    this.bookAppointmentService
      .getAvailableSlots(getSlot)
      .subscribe({
        next: (response: any) => {
          const data = response.responseBody;
          this.availableTimes = [...data];
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  next() {
    this.first = this.first + this.rows;
  }

  prev() {
    this.first = this.first - this.rows;
  }

  reset() {
    this.first = 0;
  }

  pageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
  }

  isLastPage(): boolean {
    return this.consultants ? this.first + this.rows >= this.consultants.length : true;
  }

  isFirstPage(): boolean {
    return this.consultants ? this.first === 0 : true;
  }

  customSort(event: any) {
    this.sort = event.field;
    this.order = event.order;
  }
}
