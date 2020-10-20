import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Country} from "../../../../model/country";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {COUNTRIES} from "../../../../model/countries";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-address-add-modal',
  templateUrl: './address-add-modal.component.html',
  styleUrls: ['./address-add-modal.component.css']
})
export class AddressAddModalComponent implements OnInit, OnDestroy {

  @Input()
  private contactId: number;
  sortedCountriesForSelect: Country[];

  isSaved: boolean;
  loading: boolean;
  addressForm: FormGroup;

  preSelectedCountry: Country;
  selectedCountry: string = '';

  alertMessage: string;
  alertType: string;

  address: Address;

  private subscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private addressService: AddressService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
    this.preSelectedCountry = this.sortedCountriesForSelect.find(value => value.name === 'Germany');
    this.selectedCountry = this.preSelectedCountry.name;
  }

  ngOnInit(): void {
    this.address = new Address();
    this.createForm();
  }

  private createForm(): void {
    this.addressForm = this.fb.group({
      city: [],
      zip: [],
      street: [null, [Validators.required]]
    });
  }

  onClickSave(): void {
    this.reloadStats();

    this.address.contactId = this.contactId;
    this.address.country = this.selectedCountry;

    const city = this.addressForm.controls['city'].value
    const street = this.addressForm.controls['street'].value;
    const zip = this.addressForm.controls['zip'].value

    this.address.street = street === null ? '' : street;
    this.address.city = city === null ? '' : city;
    this.address.zip = zip === null ? '' : zip;

    this.subscription = this.addressService.addAddress(this.address).subscribe(() =>
        this.callBackOk(),
      error =>
        this.callBackError(error)
    );
  }

  reloadStats(): void {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';
  }

  callBackOk(): void {
    this.loading = false;
    this.isSaved = true;

    this.alertType = 'success'
    this.alertMessage = 'Address saved';

    this.addressForm.reset();
    this.addressService.triggerOnReloadAddressesList();
  }

  callBackError(error: any): void {
    this.isSaved = false;

    this.setAlert('danger', SubscriptionErrorHandle(error))

    if (this.alertMessage)
      this.loading = false;
  }

  setAlert(alertType: string, alertMessage: string) {
    this.alertType = alertType;
    this.alertMessage = alertMessage;
  }

  onCloseAlert(): void {
    this.alertMessage = '';
  }

  selectChangeHandler(event: any): void {
    this.selectedCountry = event.target.value;
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
