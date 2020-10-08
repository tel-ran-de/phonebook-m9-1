import {Component, Input, OnInit} from '@angular/core';
import {Phone} from "src/app/model/phone";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PhoneEditModalComponent} from "../phone-edit-modal/phone-edit-modal.component";
import {PhoneService} from "../../../../service/phone.service";

@Component({
  selector: 'app-phone-table',
  templateUrl: './phone-table.component.html',
  styleUrls: ['./phone-table.component.css']
})
export class PhoneTableComponent implements OnInit {

  @Input()
  phonesToDisplay: Phone[];

  constructor(private modalService: NgbModal,
              private phoneService: PhoneService) {
  }

  ngOnInit(): void {
  }

  onClickEdit(phoneToEdit: Phone) {
    const modalRef = this.modalService.open(PhoneEditModalComponent);
    modalRef.componentInstance.phoneToEdit = phoneToEdit;
  }

  onClickRemove(phoneId: number) {
    this.phoneService.removePhone(phoneId);
  }
}
