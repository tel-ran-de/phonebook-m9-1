import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address} from "../model/address";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/address';

  constructor(private http: HttpClient) {
  }

  getAllAddressesByContactId(contactId: number) {
    return this.http.get<Address[]>(`${this.basePath}/${(contactId)}/all`);
  }

  addAddress(address: Address) {
    return this.http.post<Address>(`${(this.basePath)}`, address);
  }

  get trigger$() {
    return this._trigger.asObservable();
  }

  triggerOnReloadAddressesList() {
    this._trigger.next();
  }

  removeAddress(addressId: number) {
    this.http.delete(`${this.basePath}/${(addressId)}`)
      .subscribe(() => this.triggerOnReloadAddressesList());
  }

  editAddress(addressToEdit: Address) {
    return this.http.put<Address>(`${this.basePath}`, addressToEdit);

  }
}
