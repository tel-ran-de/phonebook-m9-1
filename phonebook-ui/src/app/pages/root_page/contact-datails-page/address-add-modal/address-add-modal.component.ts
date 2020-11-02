import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Country} from "../../../../model/country";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {COUNTRIES} from "../../../../model/countries";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";
import {ToastService} from "../../../../service/toast.service";

@Component({
  selector: 'app-address-add-modal',
  templateUrl: './address-add-modal.component.html',
  styleUrls: ['./address-add-modal.component.css']
})
export class AddressAddModalComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;
  sortedCountriesForSelect: Country[];

  loading: boolean;
  addressForm: FormGroup;

  preSelectedCountry: Country;
  selectedCountry: string = '';

  address: Address;

  addSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private addressService: AddressService,
              private toastService: ToastService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
    this.preSelectedCountry = this.sortedCountriesForSelect.find(value => value.name === 'Germany');
    this.selectedCountry = this.preSelectedCountry.name;
  }

  ngOnInit(): void {
    this.address = new Address();
    this.createForm();
  }

  createForm(): void {
    this.addressForm = this.fb.group({
      city: [],
      zip: [],
      street: [null, [Validators.required]]
    });
  }

  onClickSave(): void {
    this.loading = true;

    this.address.contactId = this.contactId;
    this.address.country = this.selectedCountry;

    const city = this.addressForm.controls['city'].value
    const street = this.addressForm.controls['street'].value;
    const zip = this.addressForm.controls['zip'].value

    this.address.street = street === null ? '' : street;
    this.address.city = city === null ? '' : city;
    this.address.zip = zip === null ? '' : zip;

    this.addSubscription = this.addressService.addAddress(this.address)
      .subscribe(() => this.callBackOkAddAddress(), () => this.callBackErrorAddAddress());
  }

  callBackOkAddAddress(): void {
    this.loading = false;

    this.addressService.triggerOnReloadAddressesList();

    this.toastService.show('address.addressSaveOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-add-address'
    });

    this.onClickCancel();
  }

  callBackErrorAddAddress() {
    this.loading = false;

    this.toastService.show('address.addressSaveFail', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-add-address`
    });

    this.onClickCancel();
  }

  selectChangeHandler(event: any): void {
    this.selectedCountry = event.target.value;
  }

  onClickCancel(): void {
    this.addressForm.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.addSubscription)
      this.addSubscription.unsubscribe();
  }
}
