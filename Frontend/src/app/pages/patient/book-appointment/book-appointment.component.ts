import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SortEvent } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { BookAppointmentService, Pageable } from '../../../service/patient/bookAppointment.service';
import { finalize } from 'rxjs';
import { UiService } from '../../../service/ui.service';

@Component({
  selector: 'app-book-appointment',
  imports: [InputTextModule, TableModule, CommonModule, ButtonModule],
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
