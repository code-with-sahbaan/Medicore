import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

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
}
