import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address} from "../model/address";
import {Phone} from "../model/phone";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private readonly basePath = '/api/address';
  addresses: Observable<Address[]>;
  private contactId: number;

  constructor(private http: HttpClient) {
  }

  add(address: Address) {
    return this.http.post<Address>(this.basePath, address);
  }

  getById(addressId: number) {
    return this.http.get<Address>(`${this.basePath}/${addressId}`);
  }

  removeById(addressId: number) {
    return this.http.delete<Address>(`${this.basePath}/${addressId}`);
  }

  editById(addressId: number ,address: Address) {
    return this.http.put<Address>(`${this.basePath}/${addressId}`, address);
  }

  getAllAddressesByContactId(contactId: number) {
    this.contactId = contactId;
    if (!this.addresses)
      this.reload();
    return this.addresses;
  }

  reload(): void {
    this.addresses = this.http.get<Address[]>(`${this.basePath}/${(this.contactId)}/all`);
  }
}
