import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PhoneAddModalComponent} from "../phone-add-modal/add-phone-modal.component";
import {EmailAddModalComponent} from "../email-add-modal/add-email-modal.component";

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

  openModal() {
    const modalRef =  this.modalService.open(PhoneAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  openModalAddEmail() {
    const modalRef =  this.modalService.open(EmailAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }
}
