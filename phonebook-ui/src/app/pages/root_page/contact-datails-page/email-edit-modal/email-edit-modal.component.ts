import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Email} from "../../../../model/email";
import {Subscription} from "rxjs";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {EmailService} from "../../../../service/email.service";
import {ToastService} from "../../../../service/toast.service";

@Component({
  selector: 'app-email-edit-modal',
  templateUrl: './email-edit-modal.component.html',
  styleUrls: ['./email-edit-modal.component.css']
})
export class EmailEditModalComponent implements OnInit, OnDestroy {

  @Input()
  emailToEdit: Email;

  loading: boolean;
  emailEditForm: FormGroup;

  emailEditSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private emailService: EmailService,
              private toastService: ToastService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.emailEditForm = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });

    this.setFormValue(this.emailToEdit);
  }

  onClickSave(): void {
    this.loading = true;

    this.emailToEdit.email = this.emailEditForm.controls['email'].value;

    this.emailEditSubscription = this.emailService.editEmail(this.emailToEdit)
      .subscribe(() => this.callBackOkEditEmail(), () => this.callBackErrorEditEmail());
  }

  callBackOkEditEmail(): void {
    this.loading = false;

    this.emailService.triggerOnReloadEmailList();

    this.toastService.show('Edit email success', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-edit-email'
    });

    this.onClickCancel();
  }

  callBackErrorEditEmail(): void {
    this.loading = false;

    this.toastService.show('Edit email failed', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-edit-email`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.emailEditForm.reset();
    this.activeModal.close();
  }

  setFormValue(emailToEdit: Email): void {
    this.emailEditForm.controls['email'].setValue(emailToEdit.email);
  }

  ngOnDestroy(): void {
    if (this.emailEditSubscription)
      this.emailEditSubscription.unsubscribe();
  }
}
