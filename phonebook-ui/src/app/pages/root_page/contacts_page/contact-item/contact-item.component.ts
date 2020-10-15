import {Component, Input, OnInit} from '@angular/core';
import {Contact} from 'src/app/model/contact';
import {ContactRemoveModalComponent} from "../contact-remove-modal/contact-remove-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-contact-item',
  templateUrl: './contact-item.component.html',
  styleUrls: ['./contact-item.component.css']
})
export class ContactItemComponent implements OnInit {
  @Input()
  contactItem: Contact;

  isSelected: boolean;

  constructor(private modalService: NgbModal) {
    this.contactItem = new Contact();
  }

  ngOnInit(): void {
  }


  removeContact() {
    const modalRef = this.modalService.open(ContactRemoveModalComponent);
    modalRef.componentInstance.contactId = this.contactItem.id;
  }
}
