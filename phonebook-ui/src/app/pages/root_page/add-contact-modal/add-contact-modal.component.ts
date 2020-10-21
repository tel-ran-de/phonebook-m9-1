import {Component, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ContactService} from "../../../service/contact.service";
import {ToastService} from "../../../service/toast.service";
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

    this.contactService.addContact(this.form.value)
      .subscribe(() => this.callBackOkAddContact(), () => this.callBackErrorAddContact());
  }

  callBackOkAddContact(): void {
    this.loading = false;
    this.isSaved = true;

    if (this.router.url !== '/contacts')
      this.router.navigate(['./contacts/'])
    else this.contactService.triggerOnReloadContactsList();

    this.toastService.show('Contact saved successfully', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-add-contact'
    });

    this.onClickCancel();
  }

  callBackErrorAddContact(): void {
    this.isSaved = false;

    this.toastService.show('Add contact failed', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-add-contact`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.form.reset();
    this.activeModal.close();
  }
}
