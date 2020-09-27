import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PhoneService} from "../../../../service/phone.service";
import {Phone} from "../../../../model/phone";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  phones: Phone[];
  private getAllPhoneByContactSubscription: Subscription;

  constructor(private phoneService: PhoneService) {
  }

  ngOnInit(): void {
    this.getAllPhoneByContactSubscription = this.phoneService.getAllPhonesByContactId(this.contactId)
      .subscribe(value => this.phones = value);
  }

  ngOnDestroy(): void {
    if (this.getAllPhoneByContactSubscription)
      this.getAllPhoneByContactSubscription.unsubscribe();
  }
}
