import {Component, Input, OnInit} from '@angular/core';
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {Country} from "../../../../model/country";
import {COUNTRIES} from "../../../../model/countries";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";

@Component({
  selector: 'app-address-edit-modal',
  templateUrl: './address-edit-modal.component.html',
  styleUrls: ['./address-edit-modal.component.css']
})
export class AddressEditModalComponent implements OnInit {


  @Input()
  sortedCountriesForSelect: Country[];

  isSaved: boolean;
  loading: boolean;
  addressForm: FormGroup;

  preSelectedCountry: Country;
  selectedCountry: Country;

  alertMessage: string;
  alertType: string;
  address: Address;
  addressToEdit: Address;


  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private addressService: AddressService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
    this.preSelectedCountry = this.sortedCountriesForSelect.find(value => value.name === 'Germany');
    this.selectedCountry.name = this.preSelectedCountry.name;
  }


ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.addressForm = this.fb.group({
      city: [],
      zip: [],
      street: [null, [Validators.required]]
    });

    this.setFormValue(this.addressToEdit);
  }

  onClickSave() {
    this.reloadStats();

    this.addressToEdit.country = this.selectedCountry.name;
    this.addressToEdit.city = this.addressForm.controls['city'].value;
    this.addressToEdit.street = this.addressForm.controls['street'].value;
    this.addressToEdit.zip = this.addressForm.controls['zip'].value;

    this.addressService.editAddress(this.addressToEdit)
      .subscribe(() => this.callBackOkAddressEdit(this.addressToEdit), error => this.callBackErrorPhoneEdit(error));
  }

  reloadStats() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';
  }

  callBackOkAddressEdit(addressToEdit: Address) {
    this.loading = false;
    this.isSaved = true;

    const message = 'Address: ' + addressToEdit.country + "," + addressToEdit.city + "," + addressToEdit.street
      + "," + addressToEdit.zip + ' saved';
    this.setAlert('success', message)

    this.addressForm.reset();
    this.addressService.triggerOnReloadAddressesList();
  }

  callBackErrorPhoneEdit(error: any) {
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
    this.selectedCountry = event.target.value;
  }

  setFormValue(addressToEdit: Address) {
    this.preSelectedCountry = this.sortedCountriesForSelect
      .find(value => value.dial_code === addressToEdit.country);
    if (this.preSelectedCountry) {
      this.selectedCountry = this.preSelectedCountry;
      this.addressForm.controls['city'].setValue(addressToEdit.city);
      this.addressForm.controls['street'].setValue(addressToEdit.street);
      this.addressForm.controls['zip'].setValue(addressToEdit.zip);

    } else {
      this.setAlert('danger', "Unknown input");
    }
  }
}
