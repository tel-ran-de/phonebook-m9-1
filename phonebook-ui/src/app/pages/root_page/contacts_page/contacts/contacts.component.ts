import {Component, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from 'src/app/service/contact.service';
import {UserService} from 'src/app/service/user.service';
import {Contact} from 'src/app/model/contact';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";

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
  errorMessage: string;
  errorMessageProfile: string;

  loading: boolean;
  loadingProfile: boolean;

  getAllContactsSubscription: Subscription;
  triggerSubscription: Subscription;
  getProfileSubscription: Subscription;
  formSubscription: Subscription;

  constructor(public contactService: ContactService,
              public userService: UserService,
              private fb: FormBuilder,
              private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.getProfile();
    this.reloadContactsList();
    this.createForm();

    this.formSubscription = this.searchContactForm.get('searchInput')
      .valueChanges
      .subscribe(value => this.searchTerm = value);

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

    this.profile = value;
  }

  callProfileError(error: HttpErrorResponse): void {
    this.errorMessageProfile = this.translateService.instant('PopUpMsg.' + SubscriptionErrorHandle(error));

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

    this.contactsToDisplay = value;
  }

  callBackGetAllContactError(error: HttpErrorResponse): void {
    this.errorMessage = this.translateService.instant('PopUpMsg.' + SubscriptionErrorHandle(error));

    this.loading = false;
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
