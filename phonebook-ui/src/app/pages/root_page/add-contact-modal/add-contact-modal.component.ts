import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {ContactService} from "../../../service/contact.service";
import {SubscriptionErrorHandle} from "../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-modal-content',
  templateUrl: './add-contact-modal.component.html',
  styleUrls: ['./add-contact-modal.component.css']
})
export class AddContactModalComponent implements OnInit, OnDestroy {
  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private contactService: ContactService) {

    config.backdrop = 'static';
  }

  isSaved: boolean;
  loading: boolean;
  alertMessage: string;
  form: FormGroup;
  private subscription: Subscription;
  successMessage: string;

  ngOnInit(): void {
    this.createForm();
  }

  private createForm() {
    this.form = this.fb.group({
      firstName: [null, [Validators.required]],
      lastName: [],
      description: []
    });
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.successMessage = '';
    this.alertMessage = '';

    this.subscription = this.contactService.addContact(this.form.value).subscribe(() => {
        this.contactService.reload();
        this.loading = false;
        this.isSaved = true;
        this.successMessage = 'Contact saved successfully';
        this.form.reset();
        this.contactService.triggerOnReloadContactsList();
      },
      error => {
        this.alertMessage = SubscriptionErrorHandle(error);
        this.isSaved = false;
        if (this.alertMessage)
          this.loading = false;
      }
    );
  }

  onClickCancel() {
    this.resetModal();
  }

  private resetModal() {
    this.form.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
