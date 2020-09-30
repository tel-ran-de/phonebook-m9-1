import {Component, Input, OnInit} from '@angular/core';
import {Contact} from 'src/app/model/contact';

@Component({
  selector: 'app-contact-item',
  templateUrl: './contact-item.component.html',
  styleUrls: ['./contact-item.component.css']
})
export class ContactItemComponent implements OnInit {
  @Input()
  contactItem: Contact;

  constructor() {
    this.contactItem = new Contact();
  }

  ngOnInit(): void {
  }
}
