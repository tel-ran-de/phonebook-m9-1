import {Component, Input, OnInit} from '@angular/core';
import {Address} from "src/app/model/address";
import {AddressService} from "../../../../service/address.service";

@Component({
  selector: 'app-address-table',
  templateUrl: './address-table.component.html',
  styleUrls: ['./address-table.component.css']
})
export class AddressTableComponent implements OnInit {

  @Input()
  sortedAddressesToDisplay: Address[];

  reverseSort: boolean;
  sortBy: string;

  constructor(private addressService: AddressService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string) {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.sortedAddressesToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)

    if (this.reverseSort)
      this.sortedAddressesToDisplay.reverse();
  }

  onClickRemove(addressId: number) {
    this.addressService.removeAddress(addressId);
  }
}
