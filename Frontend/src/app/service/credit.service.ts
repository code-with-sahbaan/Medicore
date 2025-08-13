// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface BuyCredits{
    credits: number
}

@Injectable({
    providedIn: 'root',
})
export class CreditService {
    constructor(private http: HttpClient) { }

    getMyCredits(): Observable<any> {
        return this.http.get('/user/v1/getMyCredits').pipe();
    }

    createPaymentIntent(buyCredits: BuyCredits): Observable<any> {
        return this.http.post('/payment/v1/buyCredits', buyCredits).pipe();
    }

    updateCredits(buyCredits: BuyCredits): Observable<any> {
        return this.http.post('/payment/v1/updateCredits', buyCredits).pipe();
    }
}
