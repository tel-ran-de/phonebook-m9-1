import {Component, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from 'src/app/service/contact.service';
import {UserService} from 'src/app/service/user.service';
import {Contact} from 'src/app/model/contact';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit, OnDestroy {

  profile: Contact;
  contactsToDisplay: Contact[];
  searchTerm: string;

  searchContactForm: FormGroup;

  getAllContactsSubscription: Subscription;
  triggerSubscription: Subscription;
  getProfileSubscription: Subscription;
  formSubscription: Subscription;

  constructor(public contactService: ContactService,
              public userService: UserService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.getProfile();
    this.reloadContactsList();
    this.createForm();

    this.formSubscription = this.searchContactForm.get('searchInput')
      .valueChanges
      .subscribe(value => {
        this.searchTerm = value;
      });

    this.triggerSubscription = this.contactService.trigger$
      .subscribe(() => this.reloadContactsList());
  }

  reloadContactsList(): void {
    this.getAllContactsSubscription = this.contactService.getAllContacts()
      .subscribe(contactList => this.callBackGetAllContactOk(contactList));
  }

  getProfile(): void {
    this.profile = new Contact();
    this.getProfileSubscription = this.contactService.getProfile()
      .subscribe(profile => this.callBackGetProfileOk(profile));
  }

  callBackGetProfileOk(value: Contact): void {
    if (!value.firstName)
      value.firstName = 'No first name'
    this.profile = value;
  }

  callBackGetAllContactOk(value: Contact[]): void {
    this.contactsToDisplay = value;
  }

  createForm(): void {
    this.searchContactForm = this.fb.group({
      searchInput: []
    });
  }

  ngOnDestroy(): void {
    if (this.getAllContactsSubscription)
      this.getAllContactsSubscription.unsubscribe();
    if (this.getProfileSubscription)
      this.getProfileSubscription.unsubscribe();

    if (this.triggerSubscription)
      this.triggerSubscription.unsubscribe();
    if (this.formSubscription)
      this.formSubscription.unsubscribe();
  }
}
