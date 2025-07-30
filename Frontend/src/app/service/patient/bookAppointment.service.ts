import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Pageable{
    page: number,
    size: number,
    sort: string,
    order: number
}

@Injectable({ providedIn: 'root' })
export class BookAppointmentService {

    constructor(private http: HttpClient) { }

    getAllConsultants(payload: Pageable): Observable<any> {
        return this.http.post('/user/v1/getAllConsultants', payload).pipe();
    }

}
