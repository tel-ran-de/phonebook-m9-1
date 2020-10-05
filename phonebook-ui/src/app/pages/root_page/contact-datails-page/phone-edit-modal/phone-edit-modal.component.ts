import {Component, Input, OnInit} from '@angular/core';
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Phone} from "../../../../model/phone";
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs";
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

  private phoneAddSubscription: Subscription;

  preSelectedCountryCode: Country;
  selectedCountryCode: string = '';

  alertMessage: string;
  alertType: string;


  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);

  }

  ngOnInit(): void {
    this.preSelectedCountryCode = this.sortedCountriesForSelect
      .find(value => value.dial_code === this.phoneToEdit.countryCode);
    this.selectedCountryCode = this.preSelectedCountryCode.dial_code;

    this.createForm();
  }

  private createForm() {
    this.phoneForm = this.fb.group({
      phoneNumber: [null, [Validators.required, Validators.pattern("[0-9 ]{5,12}")]]
    });

    this.phoneForm.controls['phoneNumber'].setValue(this.phoneToEdit.phoneNumber);
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.phoneToEdit.countryCode = this.selectedCountryCode;
    this.phoneToEdit.phoneNumber = this.phoneForm.controls['phoneNumber'].value;

    this.phoneAddSubscription = this.phoneService.editPhone(this.phoneToEdit).subscribe(() => {
        this.loading = false;
        this.isSaved = true;

        this.alertType = 'success'
        this.alertMessage = 'Phone number: (' + this.phoneToEdit.countryCode + ")" + this.phoneToEdit.phoneNumber + ' saved';

        this.phoneForm.reset();
        this.phoneService.triggerOnReloadPhonesList();
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
    this.selectedCountryCode = event.target.value;
  }

  ngOnDestroy(): void {
    if (this.phoneAddSubscription)
      this.phoneAddSubscription.unsubscribe();
  }
}
