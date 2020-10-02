import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Phone} from "../model/phone";

@Injectable({
  providedIn: 'root'
})
export class PhoneService {

  private readonly basePath = '/api/phone';

  constructor(private http: HttpClient) {
  }

  getAllPhonesByContactId(contactId: number) {
    return this.http.get<Phone[]>(`${this.basePath}/${(contactId)}/all`);
  }

  addPhone(phone: Phone) {
    return this.http.post<Phone>(`${(this.basePath)}`, phone);
  }

  reload(contactId: number): void {
    this.getAllPhonesByContactId(contactId);
  }
}
