import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubscriptionErrorHandle} from "src/app/service/subscriptionErrorHandle";
import {Email} from "../../../../model/email";
import {EmailService} from "../../../../service/email.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-email-add-modal',
  templateUrl: './add-email-modal.component.html',
  styleUrls: ['./add-email-modal.component.css']
})
export class EmailAddModalComponent implements OnInit {

  @Input()
  private contactId: number;

  isSaved: boolean;
  loading: boolean;
  emailForm: FormGroup;

  alertMessage: string;
  alertType: string;

  email: Email = new Email();
  private emailAddSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private emailService: EmailService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.email = new Email();
    this.createForm();
  }

  private createForm() {
    this.emailForm = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.email.email = this.emailForm.controls['email'].value;
    this.email.contactId = this.contactId;

    this.emailAddSubscription = this.emailService.addEmail(this.email).subscribe(() => {
        this.loading = false;
        this.isSaved = true;

        this.alertType = 'success'
        this.alertMessage = 'Email: ' + this.email.email + ' saved';

        this.emailForm.reset();
        this.emailService.triggerOnReloadEmailList();
      },
      error => {
        this.isSaved = false;

        this.alertType = 'danger'
        this.alertMessage = SubscriptionErrorHandle(error);

        if (this.alertMessage)
          this.loading = false;
      }
    );
  }

  onCloseAlert() {
    this.alertMessage = '';
  }
}
