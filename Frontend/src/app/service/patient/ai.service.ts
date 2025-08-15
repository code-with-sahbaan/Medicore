import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface AiUserMessage {
    message: string
}

@Injectable({ providedIn: 'root' })
export class AiService {

    constructor(private http: HttpClient) { }

    askAi(payload: AiUserMessage): Observable<any> {
        return this.http.post('/aiChat/v1/askAi', payload).pipe();
    }
}
