import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";
import {Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/email';


  constructor(private http: HttpClient) {
  }

  getAllEmailsByContactId(contactId: number): Observable<any> {
    return this.http.get<Email[]>(`${this.basePath}/${(contactId)}/all`);
  }

  removeEmail(emailId: number) {
    this.http.delete(`${this.basePath}/${(emailId)}`)
      .subscribe(() => this.triggerOnReloadEmailList());
  }

  get trigger$() {
    return this._trigger.asObservable();
  }

  triggerOnReloadEmailList(): void {
    this._trigger.next();
  }

  addEmail(email: Email): Observable<any> {
    return this.http.post<Email>(`${(this.basePath)}`, email);
  }

  editEmail(emailToEdit: Email): Observable<any> {
    return this.http.put<Email>(`${(this.basePath)}`, emailToEdit);
  }
}

