import {Component, Input, OnInit} from '@angular/core';
import {ContactService} from "src/app/service/contact.service";
import {Contact} from "src/app/model/contact";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  @Input()
  contactId: number;

  contactToDisplay: Contact = new Contact();

  isEditStat: boolean = false;
  editContactForm: FormGroup;

  alertMessage: string;
  loading: boolean;
  isSaved: boolean;

  constructor(private contactService: ContactService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.contactService.getContactById(this.contactId)
      .subscribe(contact => this.callBackOkGetContact(contact), error => this.callBackErrorGetContact(error));
    this.createForm();
  }

  onClickEditContact() {
    this.setFormValue();
    this.isEditStat = true;
  }

  createForm() {
    this.editContactForm = this.fb.group({
      firstName: [null, [Validators.required]],
      lastName: [],
      description: []
    });
  }

  setFormValue() {
    this.editContactForm.controls['firstName'].setValue(this.contactToDisplay.firstName);
    this.editContactForm.controls['lastName'].setValue(this.contactToDisplay.lastName);
    this.editContactForm.controls['description'].setValue(this.contactToDisplay.description);
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';


    this.contactService.editContact(this.setContactValueToEdit()).subscribe(
      () => this.callBackOkEditContact(), error => this.callBackErrorEditContact(error));
  }

  setContactValueToEdit(): Contact {
    const contactToEdit = new Contact();
    contactToEdit.firstName = this.editContactForm.controls['firstName'].value;
    contactToEdit.lastName = this.editContactForm.controls['lastName'].value;
    contactToEdit.description = this.editContactForm.controls['description'].value;
    contactToEdit.id = this.contactId;
    return contactToEdit;
  }

  callBackOkEditContact() {
    this.isSaved = true;
    this.ngOnInit()
  }

  callBackErrorEditContact(error: any) {
    this.alertMessage = SubscriptionErrorHandle(error);
    this.isSaved = false;
    if (this.alertMessage)
      this.loading = false;
  }

  callBackOkGetContact(contact: Contact) {
    this.loading = false;
    this.contactToDisplay = contact;
    this.isEditStat = false;
  }

  callBackErrorGetContact(error: any) {
    alert(SubscriptionErrorHandle(error));
  }

  onClickCancel() {
    this.isEditStat = false
    this.alertMessage = ''
  }
}
