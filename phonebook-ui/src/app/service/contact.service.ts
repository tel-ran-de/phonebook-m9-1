import { Injectable } from '@angular/core';
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
  private readonly profilePath = '/get-profile';
  constructor(private http: HttpClient) {
  }

  getAllContacts(): Observable<Contact[]> {
    if (!this.contacts)
      this.reload();
    return this.contacts;
  }

  private reload(): void {
    this.getProfile();
    this.contacts = this.http.get<Contact[]>(`${this.contactPath}/all`);
  }

  getProfile() {
    return this.http.get<Contact>(`${this.contactPath}${this.profilePath}`);
  }

  removeContact(id: number) {
    return this.http.delete(`${this.contactPath}/${id}`).subscribe(() => this.reload());
  }
}
