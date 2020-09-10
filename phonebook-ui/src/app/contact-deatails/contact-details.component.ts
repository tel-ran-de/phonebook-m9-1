import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Contact} from "../model/contact";
import {Subscription} from "rxjs";


@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css']
})
export class ContactDetailsComponent implements OnInit, OnDestroy {

  private _contactId: number
  private _contact: Contact;
  private subscription: Subscription;
  readonly = false;

  constructor() {
  }

  ngOnInit(): void {
  }


  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }

  get contact(): Contact {
    return this._contact;
  }

  @Input()
  set contact(value: Contact) {
    this._contact = value;
    this._contactId = value.id;
  }

  readonlyStat() {
    this.readonly = !this.readonly;
  }
}
