import {Component, Input, OnInit} from '@angular/core';
import {Phone} from "../../../../model/phone";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit {

  @Input()
  phoneToDisplay: Phone;

  constructor() {
  }

  ngOnInit(): void {
  }

}
