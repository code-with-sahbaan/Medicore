import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core/index.js';
import timelinePlugin from '@fullcalendar/timeline';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DashboardService {

    constructor(private http: HttpClient) { }

    public workoutSchedules = [
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
    ];

    public calendarOptions: CalendarOptions = {
        plugins: [timelinePlugin],
        initialView: 'timelineDay', // 👈 horizontal timeline for today
        slotDuration: '01:00:00',   // 1-hour slots
        headerToolbar: false,
        slotMinWidth: 150,
        events: this.workoutSchedules,
        height: 'auto',
        nowIndicator: true
    };

    getDashboardData(): Observable<any> {
        return this.http.get('/patient/getDashboardData').pipe();
    }

}
