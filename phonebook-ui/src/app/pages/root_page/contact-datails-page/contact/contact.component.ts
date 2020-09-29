import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from "src/app/service/contact.service";
import {Contact} from "src/app/model/contact";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  contactToDisplay: Contact;
  private getContactToDisplaySubscription: Subscription;

  constructor(private contactServes: ContactService) {
  }

  ngOnInit(): void {
    this.getContactToDisplaySubscription = this.contactServes.getContactById(this.contactId)
      .subscribe(value => this.contactToDisplay = value);
  }

  ngOnDestroy(): void {
    if (this.getContactToDisplaySubscription)
      this.getContactToDisplaySubscription.unsubscribe();
  }
}
