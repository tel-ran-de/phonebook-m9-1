import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PhoneService} from "src/app/service/phone.service";
import {Phone} from "src/app/model/phone";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  phones: Phone[];
  private getAllPhoneByContactSubscription: Subscription;
  searchFormPhone: FormGroup;
  searchResultPhones: Phone[];

  constructor(private phoneService: PhoneService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchFormPhone = this.fb.group({
      searchInput: []
    })

    this.searchFormPhone.get("searchInput").valueChanges.subscribe(value => {
      if (value.length === 0)
        this.searchResultPhones = null;
      else
        this.searchResultPhones = this.search(value)
    })

    this.reloadPhoneList();
  }

  private reloadPhoneList(): void {
    this.getAllPhoneByContactSubscription = this.phoneService.getAllPhonesByContactId(this.contactId)
      .subscribe(value => this.phones = value);
  }

  sortBy(sortBy: string, reverseSort: boolean) {
    this.phones.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)
    if (reverseSort)
      this.phones.reverse();
  }

  private search(text: string) {
    return this.phones.filter(value => {
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
