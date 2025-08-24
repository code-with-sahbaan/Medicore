// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface BuyCredits{
    credits: number
}

export interface PayoutCredits{
    bankToken: string,
    credits: number,
    currency: string,
    country: string,
    account_holder_type: string
}

export interface PayoutCredits2{
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

    getExternalAccounts(): Observable<any> {
        return this.http.get('/payment/v1/getExternalAccounts').pipe();
    }

    createPaymentIntent(buyCredits: BuyCredits): Observable<any> {
        return this.http.post('/payment/v1/buyCredits', buyCredits).pipe();
    }

    updateCredits(buyCredits: BuyCredits): Observable<any> {
        return this.http.post('/payment/v1/updateCredits', buyCredits).pipe();
    }

    payoutCredits(payoutCredits: PayoutCredits): Observable<any> {
        return this.http.post('/payment/v1/payoutCredits', payoutCredits).pipe();
    }

    payoutCredits2(payoutCredits: PayoutCredits2): Observable<any> {
        return this.http.post('/payment/v1/payoutCredits2', payoutCredits).pipe();
    }

    deleteBankAccount(): Observable<any> {
        return this.http.delete('/payment/v1/deleteBankAccount').pipe();
    }

    updateVerification(): Observable<any> {
        return this.http.get('/payment/v1/updateVerification').pipe();
    }
}
