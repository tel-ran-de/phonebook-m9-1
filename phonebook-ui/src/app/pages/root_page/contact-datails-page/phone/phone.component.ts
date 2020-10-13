import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PhoneService} from "src/app/service/phone.service";
import {Phone} from "src/app/model/phone";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  private getAllPhoneByContactSubscription: Subscription;
  searchFormPhone: FormGroup;

  phonesFromServer: Phone[] = [];
  phonesToDisplay: Phone[] = [];

  errorMessage: string;
  loading: boolean;

  constructor(private phoneService: PhoneService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.loading = true
    this.searchFormPhone = this.fb.group({
      searchInput: []
    })

    this.reloadPhones();

    this.searchFormPhone.get("searchInput").valueChanges.subscribe(searchText => {
      this.phonesToDisplay = this.search(searchText);

    });

    this.phoneService.trigger$.subscribe(() => this.reloadPhones());
  }

  private reloadPhones(): void {
    this.getAllPhoneByContactSubscription = this.phoneService.getAllPhonesByContactId(this.contactId)
      .subscribe(phones => {
        this.callbackOk(phones)
      }, error => {
        this.callbackError(error);
      });
  }

  callbackOk(value: Phone[]) {
    this.errorMessage = ''
    this.loading = false
    this.phonesFromServer = value
    this.phonesToDisplay = value;
  }

  callbackError(error: any) {
    this.errorMessage = SubscriptionErrorHandle(error)
    this.loading = false
  }

  search(text: string) {
    return this.phonesFromServer.filter(value => {
      const term = text.toLowerCase();
      const valueToString = value.countryCode + " " + value.phoneNumber;
      return valueToString.toLowerCase().includes(term)
    })
  }

  ngOnDestroy(): void {
    if (this.getAllPhoneByContactSubscription)
      this.getAllPhoneByContactSubscription.unsubscribe();
  }
}
