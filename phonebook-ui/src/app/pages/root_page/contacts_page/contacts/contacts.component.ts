import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ContactService} from "../../../../service/contact.service";
import {UserService} from "../../../../service/user.service";
import {Contact} from "../../../../model/contact";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit, OnDestroy {

  private subscriptionRemove: Subscription;
  profile: Contact;

  constructor(public contactService: ContactService, public userService: UserService) {
  }

  ngOnInit(): void {
    this.getProfile()
    this.contactService.getAllContacts();
  }

  getProfile() {
    this.profile = new Contact();
    this.contactService.getProfile().subscribe(value => {
      if (!value.firstName)
        value.firstName = 'No first name added'
      this.profile = value;
    });
  }

  ngOnDestroy(): void {
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }

}
