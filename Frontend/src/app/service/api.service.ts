import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserProfile {
  email: string,
  fullName: string,
  workingHourStart: Date,
  workingHourEnd: Date,
  consultationRates: number
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) { }

  signup(payload: any): Observable<any> {
    return this.http.post('/user/v1/signup', payload).pipe();
  }

  login(payload: any): Observable<any> {
    return this.http.post('/user/login', payload).pipe();
  }

  verifyOtp(payload: any): Observable<any> {
    return this.http.post('/user/v1/verifyOtp', payload).pipe();
  }

  forgotPassword(payload: any): Observable<any> {
    return this.http.post('/user/v1/forgotPassword', payload).pipe();
  }

  getMyProfile(): Observable<any> {
    return this.http.get('/user/v1/getMyProfile').pipe();
  }

  updateProfile(payload: UserProfile): Observable<any> {
    return this.http.post('/user/v1/updateProfile', payload).pipe();
  }
}
