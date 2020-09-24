import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Contact} from "../model/contact";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  contacts: Observable<Contact[]>;
  profile: Observable<Contact>;

  private readonly contactPath = '/api/contact';
  private readonly profilePath = '/profile';

  constructor(private http: HttpClient) {
  }

  getAllContacts(): Observable<Contact[]> {
    if (!this.contacts)
      this.reload();
    return this.contacts;
  }

  reload(): void {
    this.getProfile();
    this.contacts = this.http.get<Contact[]>(`${this.contactPath}`);
  }

  getProfile() {
    return this.http.get<Contact>(`${this.contactPath}${this.profilePath}`);
  }

  removeContact(id: number) {
    return this.http.delete(`${this.contactPath}/${id}`).subscribe(() => this.reload());
  }

  addContact(contact: Contact) {
    return this.http.post<Contact>(`${this.contactPath}`, contact);
  }

  getContactById(contactId: number) {
    return this.http.get<Contact>(`${this.contactPath}/${contactId}`);
  }
}
