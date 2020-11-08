import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Email} from "../../../../model/email";
import {EmailService} from "../../../../service/email.service";
import {Subscription} from "rxjs";
import {ToastService} from "../../../../service/toast.service";

@Component({
  selector: 'app-email-add-modal',
  templateUrl: './add-email-modal.component.html',
  styleUrls: ['./add-email-modal.component.css']
})
export class EmailAddModalComponent implements OnInit, OnDestroy {

  @Input()
  private contactId: number;

  loading: boolean;
  emailAddForm: FormGroup;

  email: Email = new Email();

  emailAddSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private emailService: EmailService,
              private toastService: ToastService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.email = new Email();
    this.createForm();
  }

  private createForm() {
    this.emailAddForm = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });
  }

  onClickSave(): void {
    this.loading = true;

    this.email.email = this.emailAddForm.controls['email'].value;
    this.email.contactId = this.contactId;

    this.emailAddSubscription = this.emailService.addEmail(this.email)
      .subscribe(() => this.callBackOkAddEmail(), () => this.callBackErrorAddEmail());
  }

  callBackOkAddEmail(): void {
    this.loading = false;

    this.emailService.triggerOnReloadEmailList();

    this.toastService.show('email.emailSaveOk', {
      classname: 'bg-success text-light',
      delay: 5_000,
      id: 'pop-up-success-add-email'
    });

    this.onClickCancel();
  }

  callBackErrorAddEmail(): void {
    this.loading = false;

    this.toastService.show('email.emailSaveFail', {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pop-up-error-add-email`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.emailAddForm.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.emailAddSubscription)
      this.emailAddSubscription.unsubscribe();
  }
}
