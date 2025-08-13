// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class CreditService {
    constructor(private http: HttpClient) { }

    getMyCredits(): Observable<any> {
        return this.http.get('/user/v1/getMyCredits').pipe();
    }
}
