import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from "src/app/service/contact.service";
import {Contact} from "src/app/model/contact";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {Subscription} from "rxjs";
import {ToastService} from "../../../../service/toast.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  contactToDisplay: Contact = new Contact();

  isEditStat: boolean = false;
  editContactForm: FormGroup;

  alertMessage: string;
  loading: boolean;

  editContactSubscription: Subscription;
  getByContactByIdSubscription: Subscription;

  constructor(private contactService: ContactService,
              private fb: FormBuilder,
              private toastService: ToastService,
              private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.getContact();
    this.createForm();
  }

  getContact(): void {
    this.getByContactByIdSubscription = this.contactService.getContactById(this.contactId)
      .subscribe(contact => this.callBackOkGetContact(contact));
  }

  callBackOkGetContact(contact: Contact): void {
    this.loading = false;
    this.contactToDisplay = contact;
    this.isEditStat = false;
  }

  createForm(): void {
    this.editContactForm = this.fb.group({
      firstName: [null, [Validators.required]],
      lastName: [],
      description: []
    });
  }

  onClickEditContact(): void {
    this.setFormValue();
    this.isEditStat = true;
  }

  setFormValue() {
    this.editContactForm.controls['firstName'].setValue(this.contactToDisplay.firstName);
    this.editContactForm.controls['lastName'].setValue(this.contactToDisplay.lastName);
    this.editContactForm.controls['description'].setValue(this.contactToDisplay.description);
  }

  onClickSave(): void {
    this.loading = true;
    this.alertMessage = '';

    this.editContactSubscription = this.contactService.editContact(this.setContactValueToEdit())
      .subscribe(() => this.callBackOkEditContact(), error => this.callBackErrorEditContact(error));
  }

  setContactValueToEdit(): Contact {
    const contactToEdit = new Contact();
    contactToEdit.firstName = this.editContactForm.controls['firstName'].value;
    contactToEdit.lastName = this.editContactForm.controls['lastName'].value;
    contactToEdit.description = this.editContactForm.controls['description'].value;
    contactToEdit.id = this.contactId;
    return contactToEdit;
  }

  callBackOkEditContact(): void {
    this.toastService.show('contact.contactEditOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-edit-contact'
    });

    this.getContact();
  }

  callBackErrorEditContact(error: any): void {
    this.alertMessage = this.translateService.instant('PopUpMsg.' + SubscriptionErrorHandle(error));

    this.toastService.show('contact.contactEditFail', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-edit-contact`
    });

    if (this.alertMessage)
      this.loading = false;
  }


  onClickCancel(): void {
    this.isEditStat = false;
    this.alertMessage = '';
  }

  ngOnDestroy(): void {
    if (this.editContactSubscription)
      this.editContactSubscription.unsubscribe();
    if (this.getByContactByIdSubscription)
      this.getByContactByIdSubscription.unsubscribe();
  }
}
