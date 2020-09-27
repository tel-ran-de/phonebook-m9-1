import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EmailService} from "../../../../service/email.service";
import {Email} from "../../../../model/email";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit, OnDestroy {
  @Input()
  contactId: number;

  emails: Email[];
  private getAllEmailsByContactSubscription: Subscription;

  constructor(private emailService: EmailService) {
  }

  ngOnInit(): void {
    this.getAllEmailsByContactSubscription = this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(value => this.emails = value);
  }

  ngOnDestroy(): void {
    if (this.getAllEmailsByContactSubscription)
      this.getAllEmailsByContactSubscription.unsubscribe();
  }
}
