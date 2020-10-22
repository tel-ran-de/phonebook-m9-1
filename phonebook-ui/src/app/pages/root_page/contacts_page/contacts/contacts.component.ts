import {Component, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from 'src/app/service/contact.service';
import {UserService} from 'src/app/service/user.service';
import {Contact} from 'src/app/model/contact';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit, OnDestroy {

  profile: Contact;
  contactsFromServer: Contact[] = [];
  contactsDisplay: Contact[] = [];


  searchContactForm: FormGroup;
  errorMessage: string;
  errorMessageProfile: string;

  loading: boolean;
  getAllContactsSubscription: Subscription;
  triggerSubscription: Subscription;
  getProfileSubscription: Subscription;
  formSubscription: Subscription;
  loadingProfile: boolean;

  constructor(public contactService: ContactService,
              public userService: UserService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.getProfile();
    this.reloadContactsList();
    this.createForm();

    this.formSubscription = this.searchContactForm.get('searchInput').valueChanges.subscribe(value =>
      this.contactsDisplay = this.searchContact(value));

    this.triggerSubscription = this.contactService.trigger$
      .subscribe(() => this.reloadContactsList());
  }

  getProfile(): void {
    this.profile = new Contact();

    this.loadingProfile = true;
    this.errorMessageProfile = '';

    this.getProfileSubscription = this.contactService.getProfile()
      .subscribe(profile => this.callBackGetProfileOk(profile), error => this.callProfileError(error));
  }

  callBackGetProfileOk(value: Contact): void {
    this.loadingProfile = false;

    if (!value.firstName)
      value.firstName = 'No first name'
    this.profile = value;
  }

  callProfileError(error: HttpErrorResponse): void {
    this.errorMessageProfile = SubscriptionErrorHandle(error);

    this.loadingProfile = false;
  }

  reloadContactsList(): void {
    this.loading = true;
    this.errorMessage = '';

    this.getAllContactsSubscription = this.contactService.getAllContacts()
      .subscribe(contactList => this.callBackGetAllContactOk(contactList), error => this.callBackGetAllContactError(error));
  }

  callBackGetAllContactOk(value: Contact[]): void {
    this.loading = false;

    this.contactsDisplay = value;
    this.contactsFromServer = value;
  }

  callBackGetAllContactError(error: HttpErrorResponse) {
    this.errorMessage = SubscriptionErrorHandle(error);

    this.loading = false;
  }

  createForm(): void {
    this.searchContactForm = this.fb.group({
      searchInput: []
    });
  }

  searchContact(text: string): Contact[] {
    return this.contactsFromServer.filter(value => {
      const term = text.toLowerCase();
      const contact = value.firstName + value.lastName + value.description
      return contact.toLowerCase().includes(term);
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
