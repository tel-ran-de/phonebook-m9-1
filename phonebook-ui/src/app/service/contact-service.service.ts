import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Contact} from "../model/contact";

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  contacts: Observable<Contact[]>;
  profile: Observable<Contact>;

  private readonly contactPath = '/api/contact';
  private readonly profilePath = '/get-profile';
  private readonly contactByIdPath = 'extended';


  constructor(private http: HttpClient, private router: Router) {
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
    return this.http.get<Contact>(`${this.contactPath}/${this.profilePath}`);
  }

  getContactById(contactId: number) {
    // console.log(`${this.contactPath}/${contactId}/${this.contactByIdPath}`)
    return this.http.get<Contact>(`${this.contactPath}/${contactId}/${this.contactByIdPath}`);
  }

  removeContact(id: number) {
    return this.http.delete(`${this.contactPath}/${id}`).subscribe(() => this.reload());
  }
}
