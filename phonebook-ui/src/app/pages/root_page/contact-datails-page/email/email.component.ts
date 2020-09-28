import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EmailService} from "src/app/service/email.service";
import {Email} from "src/app/model/email";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";

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
  searchFormEmail: FormGroup;
  searchResultEmail: Email[];

  constructor(private emailService: EmailService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchFormEmail = this.fb.group({
      searchInput: []
    })

    this.searchFormEmail.get("searchInput").valueChanges.subscribe(value => {
      if (value.length === 0)
        this.searchResultEmail = null;
      else
        this.searchResultEmail = this.search(value)
    })

    this.reloadEmails();
  }

  private reloadEmails(): void {
    this.getAllEmailsByContactSubscription = this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(value => this.emails = value);
  }

  sortBy(sortBy: string, reverseSort: boolean) {
    this.emails.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)
    if (reverseSort)
      this.emails.reverse();
  }

  private search(text: string) {
    return this.emails.filter(value => {
      const term = text.toLowerCase()
      return value.email.toLowerCase().includes(term)
    })
  }

  ngOnDestroy(): void {
    if (this.getAllEmailsByContactSubscription)
      this.getAllEmailsByContactSubscription.unsubscribe();
  }
}
