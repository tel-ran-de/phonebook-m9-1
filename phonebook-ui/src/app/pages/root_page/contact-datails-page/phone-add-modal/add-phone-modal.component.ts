import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {PhoneService} from "src/app//service/phone.service";
import {Phone} from "src/app//model/phone";
import {Country} from "src/app/model/country";
import {COUNTRIES} from "src/app/model/countries";
import {SubscriptionErrorHandle} from "src/app/service/subscriptionErrorHandle";

@Component({
  selector: 'app-phone-edit-modal',
  templateUrl: './add-phone-modal.component.html',
  styleUrls: ['./add-phone-modal.component.css']
})
export class PhoneAddModalComponent implements OnInit {

  @Input()
  private contactId: number;
  sortedCountriesForSelect: Country[];

  isSaved: boolean;
  loading: boolean;
  phoneForm: FormGroup;

  private phoneAddSubscription: Subscription;

  preSelectedCountryCode: Country;
  selectedCountryCode: string = '';

  alertMessage: string;
  alertType: string;

  phone: Phone = new Phone();

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService) {
    config.backdrop = 'static';
    this.sortedCountriesForSelect = COUNTRIES.sort((countryA, countryB) => countryA.name > countryB.name ? 1 : -1);
    this.preSelectedCountryCode = this.sortedCountriesForSelect.find(value => value.name === 'Germany');
    this.selectedCountryCode = this.preSelectedCountryCode.dial_code;
  }

  ngOnInit(): void {
    this.phone = new Phone();
    this.createForm();
  }

  private createForm() {
    this.phoneForm = this.fb.group({
      phoneNumber: [null, [Validators.required, Validators.pattern("[0-9 ]{5,12}")]]
    });
  }

  onClickSave() {
    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    this.phone.countryCode = this.selectedCountryCode;
    this.phone.phoneNumber = this.phoneForm.controls['phoneNumber'].value;
    this.phone.contactId = this.contactId;

    this.phoneAddSubscription = this.phoneService.addPhone(this.phone).subscribe(() => {
        this.loading = false;
        this.isSaved = true;

        this.alertType = 'success'
        this.alertMessage = 'Phone number: (' + this.phone.countryCode + ")" + this.phone.phoneNumber + ' saved';

        this.phoneForm.reset();
        this.phoneService.triggerOnMyButton();
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
