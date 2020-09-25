import {Component, Input, OnInit} from '@angular/core';
import {PhoneService} from "../../../../service/phone.service";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit {

  @Input()
  contactId: number;

  constructor(public phoneService: PhoneService) {
  }

  ngOnInit(): void {
    this.phoneService.getAllPhonesByContactId(this.contactId);
  }

}
