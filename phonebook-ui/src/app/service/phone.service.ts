import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Phone} from "../model/phone";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class PhoneService {
  private _trigger = new Subject<void>();

  private readonly basePath = '/api/phone';

  constructor(private http: HttpClient) {
  }

  getAllPhonesByContactId(contactId: number) {
    return this.http.get<Phone[]>(`${this.basePath}/${(contactId)}/all`);
  }

  addPhone(phone: Phone) {
    return this.http.post<Phone>(`${(this.basePath)}`, phone);
  }

  get trigger$() {
    return this._trigger.asObservable();
  }

  triggerOnReloadPhonesList() {
    this._trigger.next();
  }
}
