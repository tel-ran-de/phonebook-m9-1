import {Component, Input, OnInit} from '@angular/core';
import {Address} from "src/app/model/address";

@Component({
  selector: 'app-address-table',
  templateUrl: './address-table.component.html',
  styleUrls: ['./address-table.component.css']
})
export class AddressTableComponent implements OnInit {

  @Input()
  addressesToDisplay: Address[];

  constructor() {
  }

  ngOnInit(): void {
  }
}
