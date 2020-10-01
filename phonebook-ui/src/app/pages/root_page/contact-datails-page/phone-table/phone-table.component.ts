import {Component, Input, OnInit} from '@angular/core';
import {Phone} from "src/app/model/phone";

@Component({
  selector: 'app-phone-table',
  templateUrl: './phone-table.component.html',
  styleUrls: ['./phone-table.component.css']
})
export class PhoneTableComponent implements OnInit {

  @Input()
  phonesToDisplay: Phone[];
  @Input()
  isSearchTable: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

  tableId(): string {
    return this.isSearchTable ? "phones-table-search" : "phones-table";
  }
}
