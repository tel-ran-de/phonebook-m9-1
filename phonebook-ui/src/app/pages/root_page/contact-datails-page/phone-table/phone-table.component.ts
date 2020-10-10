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

  reverseSort: boolean;
  sortBy: string;

  constructor(private modalService: NgbModal,
              private phoneService: PhoneService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string) {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.phonesToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)

    if (this.reverseSort)
      this.phonesToDisplay.reverse();
  }

  onClickEdit(phoneToEdit: Phone) {
    const modalRef = this.modalService.open(PhoneEditModalComponent);
    modalRef.componentInstance.phoneToEdit = phoneToEdit;
  }

  onClickRemove(phoneId: number) {
    this.phoneService.removePhone(phoneId);
  }
}
