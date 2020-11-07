import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {ContactService} from "../../../../service/contact.service";
import {ToastService} from "../../../../service/toast.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-contact-remove-modal',
  templateUrl: './contact-remove-modal.component.html',
  styleUrls: ['./contact-remove-modal.component.css']
})
export class ContactRemoveModalComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  alertType: string;
  alertMessage: string;

  loading: boolean;
  isChecked: boolean;
  hasNoError: boolean;

  removeContactSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private contactService: ContactService,
              private toastService: ToastService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.hasNoError = true;
    this.alertType = 'info';
  }

  onClickRemove(): void {
    this.isChecked = true;
    this.loading = true;
    this.hasNoError = true;

    this.removeContactSubscription = this.contactService.removeContact(this.contactId)
      .subscribe(() => this.callbackOk(), () => this.callbackError());
  }

  onClickCancel(): void {
    this.activeModal.close();
  }

  callbackOk(): void {
    this.loading = false;

    this.contactService.triggerOnReloadContactsList();

    this.toastService.show('contact.contactRemoveOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-removed-contact'
    });

    this.onClickCancel();
  }

  callbackError(): void {
    this.loading = false;
    this.isChecked = false;
    this.hasNoError = false;

    this.onClickCancel();
  }

  ngOnDestroy(): void {
    if (this.removeContactSubscription)
      this.removeContactSubscription.unsubscribe();
  }
}
