import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {AddressService} from "../../../../service/address.service";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;
  private addressGetAllContactSubscription: Subscription;

  constructor(public addressService: AddressService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    // this.addressGetAllContactSubscription = this.addressService.getAllAddressesByContactId(this.contactId).subscribe();
    console.log(this.contactId);
    this.addressService.getAllAddressesByContactId(this.contactId)
  }

  ngOnDestroy(): void {
    // this.contactId = null;
    // this.addressGetAllContactSubscription.unsubscribe();
  }

}
