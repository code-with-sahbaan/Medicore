import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core/index.js';
import timelinePlugin from '@fullcalendar/timeline';
import { Observable } from 'rxjs';

export interface Workout {
    title: string,
    start: number,
    end: number
}

@Injectable({ providedIn: 'root' })
export class DashboardService {

    constructor(private http: HttpClient) { }

    public workoutSchedules: Workout[] = [];

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
        return this.http.get('/patient/v1/getDashboardData').pipe();
    }

    getDashboardDataForDoctor(): Observable<any> {
        return this.http.get('/doctor/v1/getDashboardDataForDoctor').pipe();
    }

    addWorkout(payload: Workout){
        return this.http.post('/patient/v1/addWorkout', payload).pipe();
    }

}
