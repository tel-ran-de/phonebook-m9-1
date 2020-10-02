import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {CountryISO, SearchCountryField, TooltipLabel} from 'ngx-intl-tel-input';
import {PhoneService} from "src/app//service/phone.service";
import {Phone} from "src/app//model/phone";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-phone-edit-modal',
  templateUrl: './add-phone-modal.component.html',
  styleUrls: ['./add-phone-modal.component.css']
})
export class PhoneAddModalComponent implements OnInit {

  separateDialCode = false;
  SearchCountryField = SearchCountryField;
  TooltipLabel = TooltipLabel;
  CountryISO = CountryISO;
  preferredCountries: CountryISO[] = [
    CountryISO.Germany,
    CountryISO.Russia,
    CountryISO.UnitedStates,
  ];

  @Input()
  private contactId: number;

  constructor(private config: NgbModalConfig,
              public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private phoneService: PhoneService) {

    config.backdrop = 'static';
  }

  isSaved: boolean;
  loading: boolean;

  phoneForm: FormGroup;
  private phoneAddSubscription: Subscription;
  itemName = 'phone';

  preSelectedCountry: string;
  value: any;
  alertMessage: string;
  alertType: string;

  ngOnInit(): void {
    this.createForm()

  }

  private createForm() {
    this.phoneForm = new FormGroup({
      phone: new FormControl(null, [Validators.required])
    });
  }

  onClickSave() {

    this.isSaved = false;
    this.loading = true;
    this.alertMessage = '';

    const phone: Phone = new Phone();
    phone.countryCode = this.phoneForm.get('phone').value.dialCode;
    phone.phoneNumber = this.phoneForm.get('phone').value.number;
    phone.contactId = this.contactId;

    this.phoneAddSubscription = this.phoneService.addPhone(phone).subscribe(() => {
        this.phoneService.reload(this.contactId);
        this.loading = false;
        this.isSaved = true;

        this.alertType = 'success'
        this.alertMessage = this.itemName
          + ': '
          + phone.countryCode
          + " "
          + phone.phoneNumber
          + ' saved successfully';

        this.phoneForm.reset();
      },
      error => {
        this.isSaved = false;
        this.alertType = 'danger'
        this.alertMessage = SubscriptionErrorHandle(error);
        this.loading = false;
      }
    );
  }

  onClickCancel() {
    this.resetModal();
  }

  private resetModal() {
    this.phoneForm.reset();
    this.activeModal.close();
  }

  ngOnDestroy(): void {
    if (this.phoneAddSubscription)
      this.phoneAddSubscription.unsubscribe();
  }

  onCloseAlert() {
    this.alertMessage = '';
  }
}
