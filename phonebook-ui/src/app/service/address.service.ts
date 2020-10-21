import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Address} from "../model/address";
import {Observable, Subject, throwError} from "rxjs";
import {SubscriptionErrorHandle} from "./subscriptionErrorHandle";
import {ToastService} from "./toast.service";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/address';

  constructor(private http: HttpClient,
              private toastService: ToastService) {
  }

  getAllAddressesByContactId(contactId: number): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.basePath}/${(contactId)}/all`)
      .pipe(catchError(error => this.handleError(error, 'get-all-address')));
  }

  addAddress(address: Address): Observable<Address> {
    return this.http.post<Address>(`${(this.basePath)}`, address)
      .pipe(catchError(error => this.handleError(error, 'add-address')));
  }

  removeAddress(addressId: number): Observable<any> {
    return this.http.delete(`${this.basePath}/${(addressId)}`)
      .pipe(catchError(error => this.handleError(error, 'remove-address')));
  }

  editAddress(addressToEdit: Address): Observable<Address> {
    return this.http.put<Address>(`${this.basePath}`, addressToEdit)
      .pipe(catchError(error => this.handleError(error, 'edit-address')));
  }

  get trigger$(): Observable<any> {
    return this._trigger.asObservable();
  }

  triggerOnReloadAddressesList(): void {
    this._trigger.next();
  }

  private handleError(error: HttpErrorResponse, popUpId: string) {
    const errorMessage = SubscriptionErrorHandle(error);

    this.toastService.show(errorMessage, {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pop-up-error-${popUpId}`
    });

    return throwError(error);
  }
}
