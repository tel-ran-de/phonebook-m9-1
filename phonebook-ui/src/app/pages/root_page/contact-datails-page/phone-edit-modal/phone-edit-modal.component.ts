import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Phone} from "../../../../model/phone";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {PhoneService} from "../../../../service/phone.service";
import {Country} from "../../../../model/country";
import {COUNTRIES} from "../../../../model/countries";
import {ToastService} from "../../../../service/toast.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-phone-edit-modal',
  templateUrl: './phone-edit-modal.component.html',
  styleUrls: ['./phone-edit-modal.component.css']
})
export class PhoneEditModalComponent implements OnInit, OnDestroy {

  sortedCountriesForSelect: Country[];

  loading: boolean;
  phoneEditForm: FormGroup;

  preSelectedCountryCode: Country;
  selectedCountryCode: string = '';

  alertMessage: string;
  phoneToEdit: Phone;

  editSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService,
              private toastService: ToastService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);

  }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.phoneEditForm = this.fb.group({
      phoneNumber: [null, [Validators.required, Validators.pattern("[0-9 ]{5,12}")]]
    });

    this.setFormValue(this.phoneToEdit);
  }

  onClickSave(): void {
    this.loading = true;
    this.alertMessage = '';

    this.phoneToEdit.countryCode = this.selectedCountryCode;
    this.phoneToEdit.phoneNumber = this.phoneEditForm.controls['phoneNumber'].value;

    this.phoneService.editPhone(this.phoneToEdit)
      .subscribe(() => this.callBackOkPhoneEdit(), () => this.callBackErrorPhoneEdit());
  }

  callBackOkPhoneEdit(): void {
    this.loading = false;

    this.phoneService.triggerOnReloadPhonesList();

    this.toastService.show('Edit phone success', {
      classname: 'bg-success text-light',
      delay: 5_000,
      id: 'pop-up-success-edit-phone'
    });

    this.onClickCancel();
  }

  callBackErrorPhoneEdit(): void {
    this.loading = false;

    this.toastService.show('Edit phone failed', {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pop-up-error-edit-phone`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.phoneEditForm.reset();
    this.activeModal.close();
  }

  onCloseAlert(): void {
    this.alertMessage = '';
  }

  onChangeSelectedElement(event: any): void {
    this.selectedCountryCode = event.target.value;
  }

  setFormValue(phoneToEdit: Phone): void {
    this.preSelectedCountryCode = this.sortedCountriesForSelect
      .find(value => value.dial_code === phoneToEdit.countryCode);
    if (this.preSelectedCountryCode) {
      this.selectedCountryCode = this.preSelectedCountryCode.dial_code;
      this.phoneEditForm.controls['phoneNumber'].setValue(phoneToEdit.phoneNumber);
    } else {
      this.alertMessage = 'Unknown input';
    }
  }

  ngOnDestroy(): void {
    if (this.editSubscription)
      this.editSubscription.unsubscribe();
  }
}
