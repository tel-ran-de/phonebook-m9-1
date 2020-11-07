import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Phone} from "../model/phone";
import {Observable, Subject, throwError} from "rxjs";
import {SubscriptionErrorHandle} from "./subscriptionErrorHandle";
import {ToastService} from "./toast.service";
import {catchError} from "rxjs/operators";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class PhoneService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/phone';

  constructor(private http: HttpClient,
              private toastService: ToastService,
              private translateService: TranslateService) {
  }

  getAllPhonesByContactId(contactId: number): Observable<Phone[]> {
    return this.http.get<Phone[]>(`${this.basePath}/${(contactId)}/all`)
      .pipe(catchError(error => this.handleError(error, 'get-all-phones')));
  }

  addPhone(phone: Phone): Observable<Phone> {
    return this.http.post<Phone>(`${(this.basePath)}`, phone)
      .pipe(catchError(error => this.handleError(error, 'add-phone')));
  }

  editPhone(phoneToEdit: Phone): Observable<Phone> {
    return this.http.put<Phone>(`${this.basePath}`, phoneToEdit)
      .pipe(catchError(error => this.handleError(error, 'edit-phone')));
  }

  removePhone(phoneId: number): Observable<any> {
    return this.http.delete(`${this.basePath}/${(phoneId)}`)
      .pipe(catchError(error => this.handleError(error, 'remove-phone')));
  }

  get trigger$(): Observable<any> {
    return this._trigger.asObservable();
  }

  triggerOnReloadPhonesList(): void {
    this._trigger.next();
  }

  private handleError(error: HttpErrorResponse, popUpId: string) {
    const errorMessage = this.translateService.instant(SubscriptionErrorHandle(error));

    this.toastService.show(errorMessage, {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pu-error-${popUpId}`
    });

    return throwError(error);
  }
}
