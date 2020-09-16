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
  showContact: boolean;
  profile: Contact;
  contactSelect: boolean;


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

  OnClickContact() {
    this.contactSelect = true;
  }
}
