import {Injectable} from '@angular/core';
import {Observable, Subject, throwError} from "rxjs";
import {Contact} from "../model/contact";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {ToastService} from "./toast.service";
import {SubscriptionErrorHandle} from "./subscriptionErrorHandle";

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  private _trigger = new Subject<void>();

  private readonly contactPath = '/api/contact';
  private readonly profilePath = '/profile';

  constructor(private http: HttpClient,
              private toastService: ToastService) {
  }

  getAllContacts(): Observable<Contact[]> {
    return this.http.get<Contact[]>(`${this.contactPath}`)
      .pipe(catchError(error => this.handleError(error, 'get-all-contacts')));
  }

  getProfile(): Observable<Contact> {
    return this.http.get<Contact>(`${this.contactPath}${this.profilePath}`)
      .pipe(catchError(error => this.handleError(error, 'get-profile')));
  }

  removeContact(id: number): Observable<any> {
    return this.http.delete(`${this.contactPath}/${id}`)
      .pipe(catchError(error => this.handleError(error, 'remove-contact')));
  }

  addContact(contact: Contact): Observable<Contact> {
    return this.http.post<Contact>(`${this.contactPath}`, contact)
      .pipe(catchError(error => this.handleError(error, 'add-contact')));
  }

  getContactById(contactId: number): Observable<Contact> {
    return this.http.get<Contact>(`${this.contactPath}/${contactId}`)
      .pipe(catchError(error => this.handleError(error, 'get-contact')));
  }

  editContact(contactToEdit: Contact): Observable<Contact> {
    return this.http.put<Contact>(`${this.contactPath}`, contactToEdit)
      .pipe(catchError(error => this.handleError(error, 'edit-contact')));
  }

  get trigger$(): Observable<any> {
    return this._trigger.asObservable();
  }

  triggerOnReloadContactsList(): void {
    this._trigger.next();
  }

  private handleError(error: HttpErrorResponse, popUpId: string) {
    const errorMessage = SubscriptionErrorHandle(error);

    this.toastService.show(errorMessage, {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pu-error-${popUpId}`
    });

    return throwError(error);
  }
}
