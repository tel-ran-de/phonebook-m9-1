import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {ContactService} from "../../../service/contact.service";
import {SubscriptionErrorHandle} from "../../../service/subscriptionErrorHandler.ts";

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
  errorMessage: string;
  form: FormGroup;
  private subscription: Subscription;

  ngOnInit(): void {
    this.createForm()
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
    this.errorMessage = '';

    this.subscription = this.contactService.addContact(this.form.value).subscribe(() => {
        this.contactService.reload();
        this.loading = false;
        this.isSaved = true;
        this.form.reset();
      },
      error => {
        this.errorMessage = SubscriptionErrorHandle(error);
        this.isSaved = false;
        if (this.errorMessage)
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
