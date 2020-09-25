import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from "../../../../service/contact.service";
import {Contact} from "../../../../model/contact";
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
  private contactToDisplaySubscription: Subscription;

  constructor(private contactServes: ContactService) {
  }

  ngOnInit(): void {
    this.contactToDisplaySubscription = this.contactServes.getContactById(this.contactId).subscribe(value => this.contactToDisplay = value);
  }

  ngOnDestroy(): void {
    if (this.contactToDisplaySubscription)
      this.contactToDisplaySubscription.unsubscribe();
  }
}
