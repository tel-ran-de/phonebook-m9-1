import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {AddressService} from "src/app/service/address.service";
import {Subscription} from "rxjs";
import {Address} from "src/app/model/address";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  addresses: Address[];
  private addressGetAllContactSubscription: Subscription;
  searchFormAddress: FormGroup;
  searchResultAddresses: Address[];

  constructor(private addressService: AddressService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchFormAddress = this.fb.group({
      searchInput: []
    })

    this.searchFormAddress.get("searchInput").valueChanges.subscribe(value => {
      if (value.length === 0)
        this.searchResultAddresses = null;
      else
        this.searchResultAddresses = this.search(value)
    })

    this.reloadAddresses();
  }

  private reloadAddresses(): void {
    this.addressGetAllContactSubscription = this.addressService.getAllAddressesByContactId(this.contactId)
      .subscribe(value => this.addresses = value);
  }

  sortBy(sortBy: string, reverseSort: boolean) {
    this.addresses.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)
    if (reverseSort)
      this.addresses.reverse();
  }

  private search(text: string) {
    return this.addresses.filter(value => {
      const term = text.toLowerCase()
      const valueToString = value.country + " " + value.city + " " + value.zip + " " + value.street
      return valueToString.toLowerCase().includes(term)
    })
  }

  ngOnDestroy(): void {
    if (this.addressGetAllContactSubscription)
      this.addressGetAllContactSubscription.unsubscribe();
  }
}
