import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {Country} from "../../../../model/country";
import {COUNTRIES} from "../../../../model/countries";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-address-edit-modal',
  templateUrl: './address-edit-modal.component.html',
  styleUrls: ['./address-edit-modal.component.css']
})
export class AddressEditModalComponent implements OnInit, OnDestroy {


  @Input()
  sortedCountriesForSelect: Country[];

  loading: boolean;
  addressEditForm: FormGroup;

  preSelectedCountry: Country;
  selectedCountry: string = '';

  alertMessage: string;
  alertType: string;
  address: Address;
  addressToEdit: Address;

  private subscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private addressService: AddressService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
  }


  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.addressEditForm = this.fb.group({
      city: [null, [Validators.required]],
      zip: [],
      street: [null, [Validators.required]]
    });

    this.setFormValue(this.addressToEdit);
  }

  onClickSave(): void {
    this.loading = true;
    this.alertMessage = '';

    this.addressToEdit.country = this.selectedCountry;
    this.addressToEdit.city = this.addressEditForm.controls['city'].value;
    this.addressToEdit.street = this.addressEditForm.controls['street'].value;
    this.addressToEdit.zip = this.addressEditForm.controls['zip'].value;

    this.subscription = this.addressService.editAddress(this.addressToEdit)
      .subscribe(() => this.callBackOkAddressEdit(), error => this.callBackErrorPhoneEdit(error));
  }

  callBackOkAddressEdit(): void {
    this.loading = false;

    this.addressService.triggerOnReloadAddressesList();
    this.addressEditForm.reset();
    this.activeModal.close();
  }

  callBackErrorPhoneEdit(error: any): void {
    this.alertMessage = SubscriptionErrorHandle(error)
    this.setAlert('danger', this.alertMessage)

    if (this.alertMessage)
      this.loading = false;
  }

  setAlert(alertType: string, alertMessage: string): void {
    this.alertType = alertType;
    this.alertMessage = alertMessage;
  }

  onCloseAlert(): void {
    this.alertMessage = '';
  }

  onChangeSelectedElement(event: any): void {
    this.selectedCountry = event.target.value;
  }

  setFormValue(addressToEdit: Address): void {
    this.preSelectedCountry = this.sortedCountriesForSelect
      .find(value => value.name === addressToEdit.country);

    if (this.preSelectedCountry) {
      this.selectedCountry = this.preSelectedCountry.name;
      this.addressEditForm.controls['city'].setValue(addressToEdit.city);
      this.addressEditForm.controls['street'].setValue(addressToEdit.street);
      this.addressEditForm.controls['zip'].setValue(addressToEdit.zip);
    } else {
      this.setAlert('danger', "Unknown input");
    }
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
