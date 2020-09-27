import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address} from "../model/address";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private readonly basePath = '/api/address';

  constructor(private http: HttpClient) {
  }

  getAllAddressesByContactId(contactId: number) {
    return this.http.get<Address[]>(`${this.basePath}/${(contactId)}/all`);
  }
}
