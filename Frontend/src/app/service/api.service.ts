import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  signup(payload: any): Observable<any> {
    return this.http.post('/common/signup', payload).pipe();
  }
}
