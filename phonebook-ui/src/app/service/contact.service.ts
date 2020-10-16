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

  contacts: Observable<Contact[]>;
  profile: Observable<Contact>;

  private readonly contactPath = '/api/contact';
  private readonly profilePath = '/profile';

  constructor(private http: HttpClient,
              private toastService: ToastService) {
  }

  getAllContacts(): Observable<Contact[]> {
    if (!this.contacts)
      this.reload();
    return this.contacts;
  }

  reload(): void {
    this.getProfile();
    this.contacts = this.http.get<Contact[]>(`${this.contactPath}`)
      .pipe(catchError(error => this.errorHandle(error)));
  }

  getProfile(): Observable<Contact> {
    return this.http.get<Contact>(`${this.contactPath}${this.profilePath}`);
  }

  removeContact(id: number): Observable<any> {
    return this.http.delete(`${this.contactPath}/${id}`);
  }

  addContact(contact: Contact): Observable<Contact> {
    return this.http.post<Contact>(`${this.contactPath}`, contact)
      .pipe(catchError(error => this.errorHandle(error)));
  }

  getContactById(contactId: number) {
    return this.http.get<Contact>(`${this.contactPath}/${contactId}`);
  }

  editContact(contactToEdit: Contact): Observable<Contact> {
    return this.http.put<Contact>(`${this.contactPath}`, contactToEdit);
  }

  get trigger$() {
    return this._trigger.asObservable();
  }

  triggerOnReloadContactsList() {
    this._trigger.next();
  }

  private errorHandle(error: HttpErrorResponse) {
    const errorMessage = SubscriptionErrorHandle(error);
    this.toastService.show('Error!', {classname: 'bg-danger text-light', delay: 10000});
    if (errorMessage !== '')
      this.toastService.show(errorMessage, {classname: 'bg-danger text-light', delay: 10000});
    return throwError(error);
  }
}
