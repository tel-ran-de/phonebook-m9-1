import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Phone} from "../model/phone";
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
