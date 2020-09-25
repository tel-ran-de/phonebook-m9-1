import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private readonly basePath = '/api/email';
  private contactId: number;
  emails: Observable<Email[]>;

  constructor(private http: HttpClient) {
  }

  getAllEmailsByContactId(contactId: number) {
    this.contactId = contactId;
    if (!this.emails)
      this.reload();
    return this.emails;
  }

  reload(): void {
    this.emails = this.http.get<Email[]>(`${this.basePath}/${(this.contactId)}/all`);
  }
}
