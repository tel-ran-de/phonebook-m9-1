import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PhoneService} from "src/app//service/phone.service";
import {Phone} from "src/app//model/phone";
import {Country} from "src/app/model/country";
import {COUNTRIES} from "src/app/model/countries";
import {Subscription} from "rxjs";
import {ToastService} from "../../../../service/toast.service";

@Component({
  selector: 'app-phone-add-modal',
  templateUrl: './add-phone-modal.component.html',
  styleUrls: ['./add-phone-modal.component.css']
})
export class PhoneAddModalComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;
  sortedCountriesForSelect: Country[];

  loading: boolean;
  phoneAddForm: FormGroup;

  preSelectedCountryCode: Country;
  selectedCountryCode: string = '';

  phone: Phone = new Phone();

  addPhoneSubscription: Subscription = new Subscription();

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService,
              private toastService: ToastService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
    this.preSelectedCountryCode = this.sortedCountriesForSelect.find(value => value.name === 'Germany');
    this.selectedCountryCode = this.preSelectedCountryCode.dial_code;
  }

  ngOnInit(): void {
    this.phone = new Phone();
    this.createForm();
  }

  createForm(): void {
    this.phoneAddForm = this.fb.group({
      phoneNumber: [null, [Validators.required, Validators.pattern("[0-9 ]{5,12}")]]
    });
  }

  onClickSave() {
    this.loading = true;

    this.phone.countryCode = this.selectedCountryCode;
    this.phone.phoneNumber = this.phoneAddForm.controls['phoneNumber'].value;
    this.phone.contactId = this.contactId;

    this.addPhoneSubscription = this.phoneService.addPhone(this.phone)
      .subscribe(() => this.callBackOkAddPhone(), () => this.callBackErrorAddPhone());
  }

  callBackOkAddPhone(): void {
    this.loading = false;

    this.phoneService.triggerOnReloadPhonesList();

    this.toastService.show('Phone saved', {
      classname: 'bg-success text-light',
      delay: 5_000,
      id: 'pop-up-success-add-phone'
    });

    this.onClickCancel();
  }

  callBackErrorAddPhone(): void {
    this.loading = false;

    this.toastService.show('Add phone failed', {
      classname: `bg-danger text-light`,
      delay: 7_000,
      id: `pop-up-error-add-phone`
    });

    this.onClickCancel();
  }


  selectChangeHandler(event: any): void {
    this.selectedCountryCode = event.target.value;
  }

  onClickCancel(): void {
    this.phoneAddForm.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.addPhoneSubscription)
      this.addPhoneSubscription.unsubscribe();
  }
}
