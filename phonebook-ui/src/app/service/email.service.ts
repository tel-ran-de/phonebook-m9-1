import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private readonly basePath = '/api/email';
  constructor(private http: HttpClient) {
  }

  getAllEmailsByContactId(contactId: number) {
    return this.http.get<Email[]>(`${this.basePath}/${(contactId)}/all`);
  }
}
