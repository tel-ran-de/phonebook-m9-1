import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/email';


  constructor(private http: HttpClient) {
  }

  getAllEmailsByContactId(contactId: number) {
    return this.http.get<Email[]>(`${this.basePath}/${(contactId)}/all`);
  }

  removeEmail(emailId: number) {
    this.http.delete(`${this.basePath}/${(emailId)}`)
      .subscribe(() => this.triggerOnReloadEmailList());
  }

  get trigger$() {
    return this._trigger.asObservable();
  }

  triggerOnReloadEmailList() {
    this._trigger.next();
  }

  addEmail(email: Email) {
      return this.http.post<Email>(`${(this.basePath)}`, email);
    }
  }

