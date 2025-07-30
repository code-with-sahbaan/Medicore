import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Pageable{
    page: number,
    size: number,
    sort: string,
    order: number
}

export interface GetSlots{
    doctorEmail: string,
    appointmentDate: Date
}

@Injectable({ providedIn: 'root' })
export class BookAppointmentService {

    constructor(private http: HttpClient) { }

    getAllConsultants(payload: Pageable): Observable<any> {
        return this.http.post('/user/v1/getAllConsultants', payload).pipe();
    }

    getAvailableSlots(payload: GetSlots): Observable<any> {
        return this.http.post('/appointment/v1/getAvailableSlots', payload).pipe();
    }

}
