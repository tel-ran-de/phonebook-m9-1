import {Component, Input, OnInit} from '@angular/core';
import {Phone} from "src/app/model/phone";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PhoneEditModalComponent} from "../phone-edit-modal/phone-edit-modal.component";

@Component({
  selector: 'app-phone-table',
  templateUrl: './phone-table.component.html',
  styleUrls: ['./phone-table.component.css']
})
export class PhoneTableComponent implements OnInit {

  @Input()
  phonesToDisplay: Phone[];

  constructor(private modalService: NgbModal) {
  }

  ngOnInit(): void {
  }

  onClickEdit(phoneToEdit: Phone) {
    const modalRef = this.modalService.open(PhoneEditModalComponent);
    modalRef.componentInstance.phoneToEdit = phoneToEdit;
  }
}
