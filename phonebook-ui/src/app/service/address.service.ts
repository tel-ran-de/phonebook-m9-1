import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address} from "../model/address";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private readonly basePath = '/api/address';

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
}
