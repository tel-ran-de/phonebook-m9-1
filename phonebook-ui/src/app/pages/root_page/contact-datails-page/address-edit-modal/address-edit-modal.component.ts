import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {Country} from "../../../../model/country";
import {COUNTRIES} from "../../../../model/countries";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";
import {ToastService} from "../../../../service/toast.service";

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
  address: Address;
  addressToEdit: Address;

  editSubscription: Subscription;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private addressService: AddressService,
              private toastService: ToastService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
  }


  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.addressEditForm = this.fb.group({
      city: [],
      zip: [],
      street: [null, [Validators.required]]
    });

    this.setFormValue(this.addressToEdit);
  }

  onClickSave(): void {
    this.loading = true;

    this.addressToEdit.country = this.selectedCountry;
    this.addressToEdit.city = this.addressEditForm.controls['city'].value;
    this.addressToEdit.street = this.addressEditForm.controls['street'].value;
    this.addressToEdit.zip = this.addressEditForm.controls['zip'].value;

    this.editSubscription = this.addressService.editAddress(this.addressToEdit)
      .subscribe(() => this.callBackOkAddressEdit(), () => this.callBackErrorAddressEdit());
  }

  callBackOkAddressEdit(): void {
    this.loading = false;

    this.addressService.triggerOnReloadAddressesList();

    this.toastService.show('Edit address success', {
      classname: 'bg-success text-light',
      delay: 5_000,
      id: 'pop-up-success-edit-address'
    });

    this.onClickCancel();
  }

  callBackErrorAddressEdit(): void  {
    this.loading = false;

    this.toastService.show('Edit address failed', {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pop-up-error-edit-address`
    });

    this.onClickCancel();
  }

  onClickCancel(): void {
    this.addressEditForm.reset();
    this.activeModal.close();
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
    } else
      this.alertMessage = 'Unknown input';
  }

  ngOnDestroy(): void {
    if (this.editSubscription)
      this.editSubscription.unsubscribe();
  }
}
