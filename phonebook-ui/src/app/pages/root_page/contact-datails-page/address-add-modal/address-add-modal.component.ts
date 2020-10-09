import {Component, Input, OnInit} from '@angular/core';
import {Country} from "../../../../model/country";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {COUNTRIES} from "../../../../model/countries";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {Address} from "../../../../model/address";
import {AddressService} from "../../../../service/address.service";

@Component({
  selector: 'app-address-add-modal',
  templateUrl: './address-add-modal.component.html',
  styleUrls: ['./address-add-modal.component.css']
})
export class AddressAddModalComponent implements OnInit {

  @Input()
  private contactId: number;
  sortedCountriesForSelect: Country[];

  isSaved: boolean;
  loading: boolean;
  addressForm: FormGroup;

  private addressAddSubscription: Subscription;

  preSelectedCountry: Country;
  selectedCountry: string = '';

  alertMessage: string;
  alertType: string;

  address: Address;

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

  private createForm() {
    this.addressForm = this.fb.group({
      city: [],
      zip: [],
      street: [null, [Validators.required]]
    });
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.address.contactId = this.contactId;
    this.address.country = this.selectedCountry;

    const city = this.addressForm.controls['city'].value
    const street = this.addressForm.controls['street'].value;
    const zip = this.addressForm.controls['zip'].value

    this.address.street = street === null ? '' : street;
    this.address.city = city === null ? '' : city;
    this.address.zip = zip === null ? '' : zip;

    this.addressAddSubscription = this.addressService.addAddress(this.address).subscribe(() => {
        this.loading = false;
        this.isSaved = true;

        this.alertType = 'success'
        this.alertMessage = 'Address saved';

        this.addressForm.reset();
        this.addressService.triggerOnReloadAddressesList();
      },
      error => {
        this.isSaved = false;

        this.alertType = 'danger'
        this.alertMessage = SubscriptionErrorHandle(error);

        if (this.alertMessage)
          this.loading = false;
      }
    );
  }

  onCloseAlert() {
    this.alertMessage = '';
  }

  selectChangeHandler(event: any) {
    this.selectedCountry = event.target.value;
  }

  ngOnDestroy(): void {
    if (this.addressAddSubscription)
      this.addressAddSubscription.unsubscribe();
  }
}
