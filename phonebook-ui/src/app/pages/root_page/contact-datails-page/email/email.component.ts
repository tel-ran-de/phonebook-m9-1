import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EmailService} from "src/app/service/email.service";
import {Email} from "src/app/model/email";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit, OnDestroy {
  @Input()
  contactId: number;

  emailsFromServer: Email[] = [];
  emailsToDisplay: Email[] = [];

  private getAllEmailsByContactSubscription: Subscription;
  searchFormEmail: FormGroup;
  errorMessage: string;
  loading: boolean;

  constructor(private emailService: EmailService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.searchFormEmail = this.fb.group({
      searchInput: []
    })

    this.searchFormEmail.get("searchInput").valueChanges.subscribe(searchText => {
      if (searchText.length === 0)
        this.emailsToDisplay = this.emailsFromServer;
      else
        this.emailsToDisplay = this.search(searchText);
    });

    this.reloadEmails();
  }

  private reloadEmails(): void {
    this.getAllEmailsByContactSubscription = this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(value => {
        this.errorMessage = ''
        this.loading = false
        this.emailsFromServer = value
        this.emailsToDisplay = value;
      }, error => {
        this.errorMessage = SubscriptionErrorHandle(error)
        this.loading = false
      });

  }

  private search(text: string) {
    return this.emailsFromServer.filter(value => {
      const term = text.toLowerCase()
      return value.email.toLowerCase().includes(term)
    })
  }

  ngOnDestroy(): void {
    if (this.getAllEmailsByContactSubscription)
      this.getAllEmailsByContactSubscription.unsubscribe();
  }
}
