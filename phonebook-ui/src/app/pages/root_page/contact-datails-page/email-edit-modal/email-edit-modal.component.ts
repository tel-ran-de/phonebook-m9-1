import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Email} from "../../../../model/email";
import {Subscription} from "rxjs";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {EmailService} from "../../../../service/email.service";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-email-edit-modal',
  templateUrl: './email-edit-modal.component.html',
  styleUrls: ['./email-edit-modal.component.css']
})
export class EmailEditModalComponent implements OnInit {

  @Input()
  emailToEdit: Email;

  isSaved: boolean;
  loading: boolean;
  emailEditForm: FormGroup;

  alertMessage: string;
  alertType: string;

  private emailAddSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private emailService: EmailService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.emailEditForm = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });

    this.setFormValue(this.emailToEdit);
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.emailToEdit.email = this.emailEditForm.controls['email'].value;

    this.emailAddSubscription = this.emailService.editEmail(this.emailToEdit)
      .subscribe(() => this.callBackOkEditEmail(), error => this.callBackErrorEditEmail(error));
  }

  callBackOkEditEmail() {
    this.loading = false;
    this.isSaved = true;

    this.alertType = 'success'
    this.alertMessage = 'Email: ' + this.emailToEdit.email + ' saved';

    this.emailEditForm.reset();
    this.emailService.triggerOnReloadEmailList();
  }

  callBackErrorEditEmail(error: any) {
    this.isSaved = false;

    this.alertType = 'danger'
    this.alertMessage = SubscriptionErrorHandle(error);

    if (this.alertMessage)
      this.loading = false;
  }

  onCloseAlert() {
    this.alertMessage = '';
  }

  setFormValue(emailToEdit: Email) {
    this.emailEditForm.controls['email'].setValue(emailToEdit.email);
  }
}
