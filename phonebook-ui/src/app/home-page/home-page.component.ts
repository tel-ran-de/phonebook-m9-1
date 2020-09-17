import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {Subscription} from "rxjs";
import {Contact} from "../model/contact";
import {ContactService} from "../service/contact.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit, OnDestroy {

  private subscriptionRemove: Subscription;
  contactIdToDisplay: Contact;
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

  onClickRemove(id: number) {
    this.subscriptionRemove = this.contactService.removeContact(id);
  }


  setContactToDisplay(contact: Contact) {
    this.contactIdToDisplay = contact;
  }


  onClickSearch() {

  }

  ngOnDestroy(): void {
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }
}
