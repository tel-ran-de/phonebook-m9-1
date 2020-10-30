import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {EmailService} from "src/app/service/email.service";
import {Email} from "src/app/model/email";
import {FormBuilder, FormGroup} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EmailAddModalComponent} from "../email-add-modal/add-email-modal.component";
import {Subscription} from "rxjs";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  emailsToDisplay: Email[] = [];
  searchTerm: string;

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
    this.createForm();
    this.reloadEmails();

    this.formSubscription = this.searchFormEmail.get("searchInput")
      .valueChanges
      .subscribe(searchText => this.searchTerm = searchText);

    this.triggerSubscription = this.emailService.trigger$
      .subscribe(() => {
        this.emailsToDisplay = [];
        this.reloadEmails();
      });
  }

  reloadEmails(): void {
    this.loading = true;
    this.getAllSubscription = this.emailService.getAllEmailsByContactId(this.contactId)
      .subscribe(email => this.callbackOkGetAllEmails(email), error => this.callbackErrorGetAllEmails(error));
  }

  callbackOkGetAllEmails(value: Email[]): void {
    this.errorMessage = '';
    this.loading = false;
    this.emailsToDisplay = value;
  }

  callbackErrorGetAllEmails(error: HttpErrorResponse): void {
    this.loading = false;
    this.errorMessage = SubscriptionErrorHandle(error);
  }

  openModalAddEmail(): void {
    const modalRef = this.modalService.open(EmailAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  createForm(): void {
    this.searchFormEmail = this.fb.group({
      searchInput: []
    });
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
