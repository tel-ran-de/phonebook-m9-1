import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ContactService} from "../../../service/contact.service";
import {ToastService} from "../../../service/toast.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-modal-content',
  templateUrl: './add-contact-modal.component.html',
  styleUrls: ['./add-contact-modal.component.css']
})
export class AddContactModalComponent implements OnInit, OnDestroy {

  isSaved: boolean;
  loading: boolean;
  alertMessage: string;
  form: FormGroup;

  addContactSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private contactService: ContactService,
              private toastService: ToastService,
              private router: Router) {

    config.backdrop = 'static';
  }


  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.form = this.fb.group({
      firstName: [null, [Validators.required]],
      lastName: [],
      description: []
    });
  }

  onClickSave(): void {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.addContactSubscription = this.contactService.addContact(this.form.value)
      .subscribe(contact => this.callBackOkAddContact(contact.id), () => this.callBackErrorAddContact());
  }

  callBackOkAddContact(contactId: number): void {
    this.loading = false;
    this.isSaved = true;

    this.redirectTo('/contacts/' + contactId);

    this.toastService.show('contact.contactAddOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-add-contact'
    });

    this.onClickCancel();
  }

  redirectTo(uri: string) {
    this.router.navigateByUrl('contacts', {skipLocationChange: true}).then(() =>
      this.router.navigate([uri]));
  }

  callBackErrorAddContact(): void {
    this.isSaved = false;

    this.toastService.show('contact.contactAddFail', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-add-contact`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.form.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.addContactSubscription)
      this.addContactSubscription.unsubscribe();
  }
}
