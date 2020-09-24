import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Email} from "../model/email";
import {Phone} from "../model/phone";
import {Contact} from "../model/contact";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PhoneService {

  private readonly basePath = '/api/phone';

  phones: Observable<Phone[]>;
  private contactId: number;


  constructor(private http: HttpClient) {
  }

  add(phone: Email) {
    return this.http.post<Phone>(this.basePath, phone);
  }

  getById(phoneId: number) {
    return this.http.get<Phone>(`${this.basePath}/${phoneId}`);
  }

  removeById(phoneId: number) {
    return this.http.delete<Phone>(`${this.basePath}/${phoneId}`);
  }

  editById(phoneId: number, phone: Phone) {
    return this.http.put<Phone>(`${this.basePath}/${phoneId}`, phone);
  }

  getAllPhonesByContactId(contactId: number) {
    this.contactId = contactId;
    if (!this.phones)
      this.reload();
    return this.phones;
  }

  reload(): void {
    this.phones = this.http.get<Phone[]>(`${this.basePath}/${(this.contactId)}/all`);
  }
}
