import {Component, Input, OnInit} from '@angular/core';
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Phone} from "../../../../model/phone";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {PhoneService} from "../../../../service/phone.service";
import {Country} from "../../../../model/country";
import {COUNTRIES} from "../../../../model/countries";

@Component({
  selector: 'app-phone-edit-modal',
  templateUrl: './phone-edit-modal.component.html',
  styleUrls: ['./phone-edit-modal.component.css']
})
export class PhoneEditModalComponent implements OnInit {


  @Input()
  phoneToEdit: Phone;

  sortedCountriesForSelect: Country[];

  isSaved: boolean;
  loading: boolean;
  phoneForm: FormGroup;

  preSelectedCountryCode: Country;
  selectedCountryCode: string = '';

  alertMessage: string;
  alertType: string;


  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService) {
    config.backdrop = 'static';
    this.phoneListSortOnCreateComponent();
  }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.phoneForm = this.fb.group({
      phoneNumber: [null, [Validators.required, Validators.pattern("[0-9 ]{5,12}")]]
    });

    this.setFormValue(this.phoneToEdit);
  }

  phoneListSortOnCreateComponent() {
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
  }

  onClickSave() {
    this.reloadStats();

    this.phoneToEdit.countryCode = this.selectedCountryCode;
    this.phoneToEdit.phoneNumber = this.phoneForm.controls['phoneNumber'].value;

    this.phoneService.editPhone(this.phoneToEdit)
      .subscribe(() =>
          this.callBackOk(this.phoneToEdit),
        error =>
          this.callBackError(error)
      );
  }

  reloadStats() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';
  }

  callBackOk(phoneToEdit: Phone) {
    this.loading = false;
    this.isSaved = true;

    const message = 'Phone number: (' + phoneToEdit.countryCode + ")" + phoneToEdit.phoneNumber + ' saved';
    this.setAlert('success', message)

    this.phoneForm.reset();
    this.phoneService.triggerOnReloadPhonesList();
  }

  callBackError(error: any) {
    this.isSaved = false;

    this.setAlert('danger', SubscriptionErrorHandle(error))

    if (this.alertMessage)
      this.loading = false;
  }

  setAlert(alertType: string, alertMessage: string) {
    this.alertType = alertType;
    this.alertMessage = alertMessage;
  }

  onCloseAlert() {
    this.alertMessage = '';
  }

  onChangeSelectedElement(event: any) {
    this.selectedCountryCode = event.target.value;
  }

  setFormValue(phoneToEdit: Phone) {
    this.preSelectedCountryCode = this.sortedCountriesForSelect
      .find(value => value.dial_code === phoneToEdit.countryCode);
    if (this.preSelectedCountryCode) {
      this.selectedCountryCode = this.preSelectedCountryCode.dial_code;
      this.phoneForm.controls['phoneNumber'].setValue(phoneToEdit.phoneNumber);
    } else {
      this.setAlert('danger', "Unknown input");
    }
  }
}
