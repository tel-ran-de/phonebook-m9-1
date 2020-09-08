import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {UserService} from "../service/user.service";
import {ContactService} from "../service/contact-service.service";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit, OnDestroy {
  private subscriptionRemove: Subscription;
  contactIdToDisplay: number;
  showContact: boolean;

  constructor(public contactService: ContactService, public userService: UserService) {
  }

  ngOnInit(): void {
    this.contactService.getAllContacts();
  }

  onClickRemove(id: number) {
    this.subscriptionRemove = this.contactService.removeContact(id);
  }


  ngOnDestroy(): void {
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }


  setContactToDisplay(id: number) {
    this.showContact = true;
    this.contactIdToDisplay = id;
  }
}
