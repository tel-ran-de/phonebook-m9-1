import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {UserService} from "../service/user.service";
import {ContactService} from "../service/contact-service.service";
import {Contact} from "../model/contact";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit, OnDestroy {
  private subscriptionRemove: Subscription;
  contactIdToDisplay: Contact;
  showContact: boolean;
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
          value.firstName = 'Profile name'
        if (!value.lastName)
          value.lastName = 'Profile last name'
        this.profile = value;
      }
    );

  }

  onClickRemove(id: number) {
    this.subscriptionRemove = this.contactService.removeContact(id);
  }


  ngOnDestroy(): void {
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }


  setContactToDisplay(id: Contact) {
    this.showContact = true;
    this.contactIdToDisplay = id;
  }

  onClickSearch() {

  }
}
