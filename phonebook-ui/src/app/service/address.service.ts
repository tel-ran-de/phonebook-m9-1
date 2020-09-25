import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address} from "../model/address";
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

  getAllAddressesByContactId(contactId: number) {
    if (!this.addresses || this.contactId === contactId)
      this.reload(contactId);

    return this.addresses;
  }

  reload(contactId: number): void {
    this.addresses = this.http.get<Address[]>(`${this.basePath}/${(contactId)}/all`);
  }
}
