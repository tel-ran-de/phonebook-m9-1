import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private readonly basePath = '/api/email';

  constructor(private http: HttpClient) {
  }

  add(email: Email) {
    return this.http.post<Email>(this.basePath, email);
  }

  getById(emailId: number) {
    return this.http.get<Email>(`${this.basePath}/${emailId}`);
  }

  removeById(emailId: number) {
    return this.http.delete<Email>(`${this.basePath}/${emailId}`);
  }

  editById(emailId: number, email: Email) {
    return this.http.put<Email>(`${this.basePath}/${emailId}`, email);
  }
}
