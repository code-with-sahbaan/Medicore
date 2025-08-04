import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Event {
    title: string,
    start: Date,
    end: Date,
    allDay: boolean
}

@Injectable({ providedIn: 'root' })
export class CalendarService {

    constructor(private http: HttpClient) { }

    public events: Event[] = [];

    getAllAppointments(): Observable<any> {
        return this.http.get('/appointment/v1/getAllAppointments').pipe();
    }

}
