import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { SortEvent } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-book-appointment',
  imports: [InputTextModule, TableModule, CommonModule, ButtonModule],
  templateUrl: './book-appointment.component.html',
  styleUrl: './book-appointment.component.css'
})
export class BookAppointmentComponent {
  customers: any[] = [
    {
      id: 1000,
      name: 'James Butt',
      country: {
        name: 'Algeria',
        code: 'dz'
      },
      company: 'Benton, John B Jr',
      date: '2015-09-13',
      status: 'unqualified',
      verified: true,
      activity: 17,
      representative: {
        name: 'Ioni Bowcher',
        image: 'ionibowcher.png'
      },
      balance: 70663
    },
    {
      id: 1000,
      name: 'James Butt 02',
      country: {
        name: 'Algeria',
        code: 'dz'
      },
      company: 'Benton, John B Jr',
      date: '2015-09-13',
      status: 'unqualified',
      verified: true,
      activity: 17,
      representative: {
        name: 'Ioni Bowcher',
        image: 'ionibowcher.png'
      },
      balance: 70663
    }
  ];

  first = 0;

  rows = 10;

  isSorted: boolean = false;

  ngOnInit() {
    // this.customerService.getCustomersLarge().then((customers) => (this.customers = customers));
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
    console.log(this.first, this.rows)
  }

  isLastPage(): boolean {
    return this.customers ? this.first + this.rows >= this.customers.length : true;
  }

  isFirstPage(): boolean {
    return this.customers ? this.first === 0 : true;
  }

  customSort(event: SortEvent) {
    // if (this.isSorted == null || this.isSorted === undefined) {
    //   this.isSorted = true;
    //   this.sortTableData(event);
    // } else if (this.isSorted == true) {
    //   this.isSorted = false;
    //   this.sortTableData(event);
    // } else if (this.isSorted == false) {
    //   this.isSorted = false;
    //   // this.products = [...this.initialValue];
    //   // this.dt.reset();
    // }
    console.log(this.first, this.rows, event.field, event.order);
  }

  sortTableData(event: any) {
    event.data.sort((data1: any, data2: any) => {
      let value1 = data1[event.field];
      let value2 = data2[event.field];
      let result = null;
      if (value1 == null && value2 != null) result = -1;
      else if (value1 != null && value2 == null) result = 1;
      else if (value1 == null && value2 == null) result = 0;
      else if (typeof value1 === 'string' && typeof value2 === 'string') result = value1.localeCompare(value2);
      else result = value1 < value2 ? -1 : value1 > value2 ? 1 : 0;

      return event.order * result;
    });
  }
}
