import {Component, OnInit} from '@angular/core';
import {ContactService} from 'src/app/service/contact.service';
import {UserService} from 'src/app/service/user.service';
import {Contact} from 'src/app/model/contact';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit {

  profile: Contact;
  contactsFromServer: Contact[];
  contactsDisplay: Contact[];

  searchContactForm: FormGroup;

  constructor(public contactService: ContactService,
              public userService: UserService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.getProfile();
    this.reloadContactsList();
    this.createForm();

    this.searchContactForm.get('searchInput').valueChanges.subscribe(value =>
      this.contactsDisplay = this.searchContact(value));

    this.contactService.trigger$
      .subscribe(() => this.reloadContactsList());
  }

  private reloadContactsList() {
    this.contactService.getAllContacts()
      .subscribe(contactList => this.callBackGetAllContactOk(contactList));
  }

  getProfile() {
    this.profile = new Contact();
    this.contactService.getProfile()
      .subscribe(profile => this.callBackGetProfileOk(profile));
  }

  callBackGetProfileOk(value: Contact) {
    if (!value.firstName)
      value.firstName = 'No first name'
    this.profile = value;
  }

  callBackGetAllContactOk(value: Contact[]) {
    this.contactsDisplay = value;
    this.contactsFromServer = value;
  }

  createForm() {
    this.searchContactForm = this.fb.group({
      searchInput: []
    });
  }

  searchContact(text: string) {
    return this.contactsFromServer.filter(value => {
      const term = text.toLowerCase();
      const contact = value.firstName + value.lastName + value.description
      return contact.toLowerCase().includes(term);
    });
  }
}
