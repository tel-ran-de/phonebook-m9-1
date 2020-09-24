import {Component, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../../service/user.service";
import {EmailService} from "../../../../service/email.service";
import {PhoneService} from "../../../../service/phone.service";
import {AddressService} from "../../../../service/address.service";

@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css']
})
export class ContactDetailsComponent implements OnInit {
  public contactId: number;

  constructor(private fb: FormBuilder,
              private userService: UserService,
              public emailService: EmailService,
              public addressService: AddressService,
              public phoneService: PhoneService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.contactId = +this.route.snapshot.paramMap.get('contactId');
    this.emailService.getAllEmailsByContactId(this.contactId);
    this.phoneService.getAllPhonesByContactId(this.contactId);
    this.addressService.getAllAddressesByContactId(this.contactId);
  }

}
