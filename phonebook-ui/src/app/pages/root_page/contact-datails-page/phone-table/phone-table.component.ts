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

  constructor() {
  }

  ngOnInit(): void {
  }

}
