import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EmailService} from "src/app/service/email.service";
import {Email} from "src/app/model/email";
import {FormBuilder, FormGroup} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EmailAddModalComponent} from "../email-add-modal/add-email-modal.component";
import {Subscription} from "rxjs";
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

  searchFormEmail: FormGroup;
  errorMessage: string;
  loading: boolean;

  triggerSubscription: Subscription;
  formSubscription: Subscription;
  getAllSubscription: Subscription;

  constructor(private emailService: EmailService,
              private fb: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.searchFormEmail = this.fb.group({
      searchInput: []
    });

    this.reloadEmails();

    this.formSubscription = this.searchFormEmail.get("searchInput").valueChanges
      .subscribe(searchText => this.emailsToDisplay = this.search(searchText));

    this.triggerSubscription = this.emailService.trigger$
      .subscribe(() => {
        this.emailsToDisplay = [];
        this.reloadEmails();
      });
  }

  reloadEmails(): void {
    this.loading = true;

    this.getAllSubscription = this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(email => this.callbackOk(email), error => this.callbackError(error));
  }

  callbackOk(value: Email[]): void {
    this.errorMessage = '';
    this.loading = false;

    this.emailsFromServer = value;
    this.emailsToDisplay = value;
  }

  callbackError(error: any): void {
    this.errorMessage = SubscriptionErrorHandle(error);

    this.loading = false;
  }

  search(text: string): Email[] {
    return this.emailsFromServer.filter(emailItem => {
      const term = text.toLowerCase();
      return emailItem.email.toLowerCase().includes(term);
    });
  }

  openModalAddEmail(): void {
    const modalRef = this.modalService.open(EmailAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  ngOnDestroy(): void {
    if (this.formSubscription)
      this.formSubscription.unsubscribe();
    if (this.getAllSubscription)
      this.getAllSubscription.unsubscribe();
    if (this.triggerSubscription)
      this.triggerSubscription.unsubscribe();
  }
}
