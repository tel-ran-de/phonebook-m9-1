import {Component, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ContactService} from "../../../service/contact.service";
import {SubscriptionErrorHandle} from "../../../service/subscriptionErrorHandle";
import {ToastService} from "../../../service/toast.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Contact} from "../../../model/contact";
import {Router} from "@angular/router";

@Component({
  selector: 'app-modal-content',
  templateUrl: './add-contact-modal.component.html',
  styleUrls: ['./add-contact-modal.component.css']
})
export class AddContactModalComponent implements OnInit {

  isSaved: boolean;
  loading: boolean;
  alertMessage: string;
  form: FormGroup;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private contactService: ContactService,
              private toastService: ToastService,
              private router: Router,) {

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

  async onClickSave(): Promise<void> {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    await this.contactService.addContact(this.form.value)
      .subscribe(contact => this.callBackOkAddContact(contact), error => this.callBackErrorAddContact(error));
  }

  callBackOkAddContact(contact: Contact): void {
    this.loading = false;
    this.isSaved = true;

    this.router.navigate(['./contacts/' + contact.id])
    this.toastService.show('Contact saved successfully', {classname: 'bg-success text-light', delay: 10000});
    this.onClickCancel();
  }

  callBackErrorAddContact(error: HttpErrorResponse): void {
    this.alertMessage = SubscriptionErrorHandle(error);
    this.isSaved = false;
    if (this.alertMessage)
      this.loading = false;
  }

  onClickCancel(): void {
    this.form.reset();
    this.activeModal.close();
  }
}
