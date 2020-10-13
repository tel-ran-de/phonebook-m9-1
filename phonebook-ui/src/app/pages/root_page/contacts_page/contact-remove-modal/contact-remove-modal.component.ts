import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {ContactService} from "../../../../service/contact.service";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-contact-remove-modal',
  templateUrl: './contact-remove-modal.component.html',
  styleUrls: ['./contact-remove-modal.component.css']
})
export class ContactRemoveModalComponent implements OnInit {

  @Input()
  contactId: number;

  alertType: string;
  alertMessage: string;

  loading: boolean;
  isChecked: boolean;
  hasNoError: boolean;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private contactService: ContactService) {
    config.backdrop = 'static';
  }

  ngOnInit(): void {
    this.hasNoError = true;
    this.alertType = 'info';
    this.alertMessage = 'Deleted contact cannot be restored';
  }

  onClickRemove() {
    this.isChecked = true;
    this.loading = true;
    this.hasNoError = true;

    this.contactService.removeContact(this.contactId)
      .subscribe(() => this.callbackOk(),
        error => this.callbackError(error));
  }

  onClickCancel() {
    this.activeModal.close();
  }

  callbackOk() {
    this.loading = false;
    this.contactService.triggerOnReloadContactsList();
    this.activeModal.close();
  }

  callbackError(error: any) {
    this.loading = false;
    this.isChecked = false;
    this.hasNoError = false;

    this.alertType = 'danger';
    this.alertMessage = SubscriptionErrorHandle(error);
  }
}
