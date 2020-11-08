import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Email} from "../model/email";
import {Observable, Subject, throwError} from "rxjs";
import {SubscriptionErrorHandle} from "./subscriptionErrorHandle";
import {ToastService} from "./toast.service";
import {catchError} from "rxjs/operators";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private _trigger = new Subject<void>();

  private readonly basePath = '/api/email';

  constructor(private http: HttpClient,
              private toastService: ToastService,
              private translateService: TranslateService) {
  }

  getAllEmailsByContactId(contactId: number): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.basePath}/${(contactId)}/all`)
      .pipe(catchError(error => this.handleError(error, 'get-all-emails')));
  }

  removeEmail(emailId: number): Observable<any> {
    return this.http.delete(`${this.basePath}/${(emailId)}`)
      .pipe(catchError(error => this.handleError(error, 'remove-email')));
  }

  addEmail(email: Email): Observable<Email> {
    return this.http.post<Email>(`${(this.basePath)}`, email)
      .pipe(catchError(error => this.handleError(error, 'add-email')));
  }

  editEmail(emailToEdit: Email): Observable<Email> {
    return this.http.put<Email>(`${(this.basePath)}`, emailToEdit)
      .pipe(catchError(error => this.handleError(error, 'edit-email')));
  }

  get trigger$(): Observable<any> {
    return this._trigger.asObservable();
  }

  triggerOnReloadEmailList(): void {
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

