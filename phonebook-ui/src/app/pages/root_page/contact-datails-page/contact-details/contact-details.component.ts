import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PhoneAddModalComponent} from "../phone-add-modal/add-phone-modal.component";
import {AddressAddModalComponent} from "../address-add-modal/address-add-modal.component";


@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css'],
})
export class ContactDetailsComponent implements OnInit, OnDestroy {

  contactId: number;

  constructor(private route: ActivatedRoute, private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.contactId = +this.route.snapshot.paramMap.get('contactId');
  }

  ngOnDestroy(): void {
    this.contactId = null;
  }

  openModalAddPhone() {
    const modalRef =  this.modalService.open(PhoneAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  openModalAddAddress() {
    const modalRef =  this.modalService.open(AddressAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }
}
