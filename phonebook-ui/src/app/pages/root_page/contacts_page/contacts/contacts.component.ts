import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ContactService} from 'src/app/service/contact.service';
import {UserService} from 'src/app/service/user.service';
import {Contact} from 'src/app/model/contact';

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit, OnDestroy {

  private getProfileSubscription: Subscription;
  private getContactsSubscription: Subscription;
  profile: Contact;
  contacts: Contact[];

  constructor(public contactService: ContactService, public userService: UserService) {
  }

  ngOnInit(): void {
    this.getProfile()
    this.getProfileSubscription = this.contactService.getAllContacts()
      .subscribe(value => this.contacts = value);
  }

  getProfile() {
    this.profile = new Contact();
    this.getProfileSubscription = this.contactService.getProfile()
      .subscribe(value => {
        if (!value.firstName)
          value.firstName = 'No first name'
        this.profile = value;
      });
  }

  ngOnDestroy(): void {
    if (this.getProfileSubscription)
      this.getProfileSubscription.unsubscribe();

    if (this.getContactsSubscription)
      this.getContactsSubscription.unsubscribe();
  }

}
