import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ContactService} from "../service/contact-service.service";
import {Contact} from "../model/contact";


@Component({
  selector: 'app-contact-deatails',
  templateUrl: './contact-deatails.component.html',
  styleUrls: ['./contact-deatails.component.css']
})
export class ContactDeatailsComponent implements OnInit, OnChanges {

  @Input()
  contactId: number
  contact: Contact;
  loadinStatus: boolean;

  constructor(private contactService: ContactService) {
  }

  ngOnInit(): void {
    this.contact = new Contact()
    // this.reloadContact()
  }

  reloadContact() {
    console.log(this.contactId);
    this.contactService.getContactById(this.contactId).subscribe(value => {
      this.contact = value
      this.loadinStatus = false;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.loadinStatus = true;
    this.reloadContact();
  }
}
