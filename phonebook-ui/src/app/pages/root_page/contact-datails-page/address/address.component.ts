import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";
import {Address} from "../../../../model/address";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  private addressGetAllContactSubscription: Subscription;
  addresses: Address[];

  constructor(private addressService: AddressService) {
  }

  ngOnInit(): void {
    this.addressGetAllContactSubscription = this.addressService.getAllAddressesByContactId(this.contactId)
      .subscribe(value => this.addresses = value);
  }

  ngOnDestroy(): void {
    if (this.addressGetAllContactSubscription)
      this.addressGetAllContactSubscription.unsubscribe();
  }
}
