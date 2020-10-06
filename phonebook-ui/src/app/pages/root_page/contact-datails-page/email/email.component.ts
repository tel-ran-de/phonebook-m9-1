import {Component, Input, OnInit} from '@angular/core';
import {EmailService} from "src/app/service/email.service";
import {Email} from "src/app/model/email";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "src/app/service/subscriptionErrorHandle";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  @Input()
  contactId: number;

  emailsFromServer: Email[] = [];
  emailsToDisplay: Email[] = [];

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

    this.reloadEmails();

    this.searchFormEmail.get("searchInput").valueChanges.subscribe(searchText => {
      this.emailsToDisplay = this.search(searchText);
    });
  }

  reloadEmails(): void {
    this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(email => {
        this.callbackOk(email);
      }, error => {
        this.callbackError(error)
      });
  }

  callbackOk(value: Email[]) {
    this.errorMessage = ''
    this.loading = false
    this.emailsFromServer = value
    this.emailsToDisplay = value;
  }

  callbackError(error: any) {
    this.errorMessage = SubscriptionErrorHandle(error)
    this.loading = false
  }

  search(text: string) {
    return this.emailsFromServer.filter(emailItem => {
      const term = text.toLowerCase()
      return emailItem.email.toLowerCase().includes(term)
    })
  }
}
