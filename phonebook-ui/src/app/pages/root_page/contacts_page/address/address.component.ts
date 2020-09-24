import {Component, Input, OnInit} from '@angular/core';
import {Address} from "../../../../model/address";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit {

  @Input()
  addressToDisplay: Address;

  constructor() { }

  ngOnInit(): void {
  }

}
