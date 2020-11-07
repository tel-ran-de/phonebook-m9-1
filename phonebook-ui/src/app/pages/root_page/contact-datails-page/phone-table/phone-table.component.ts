import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Phone} from "src/app/model/phone";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PhoneEditModalComponent} from "../phone-edit-modal/phone-edit-modal.component";
import {PhoneService} from "../../../../service/phone.service";
import {ToastService} from "../../../../service/toast.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-phone-table',
  templateUrl: './phone-table.component.html',
  styleUrls: ['./phone-table.component.css']
})
export class PhoneTableComponent implements OnInit, OnDestroy {

  @Input()
  phonesToDisplay: Phone[];

  reverseSort: boolean;
  sortBy: string;

  removeSubscription: Subscription;

  constructor(private modalService: NgbModal,
              private phoneService: PhoneService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string): void {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.phonesToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1);

    if (this.reverseSort)
      this.phonesToDisplay.reverse();
  }

  onClickRemove(phoneId: number): void {
    this.removeSubscription = this.phoneService.removePhone(phoneId)
      .subscribe(() => this.callBackOkRemovePhone(), () => this.callBackErrorRemovePhone());
  }

  callBackOkRemovePhone(): void {
    this.toastService.show('phone.phoneRemoveOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-remove-phone'
    });

    this.phoneService.triggerOnReloadPhonesList();
  }

  callBackErrorRemovePhone(): void {
    this.toastService.show('phone.phoneRemoveFail', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-remove-phone`
    });
  }


  onClickEdit(phoneToEdit: Phone): void {
    const modalRef = this.modalService.open(PhoneEditModalComponent);
    modalRef.componentInstance.phoneToEdit = phoneToEdit;
  }

  ngOnDestroy(): void {
    if (this.removeSubscription)
      this.removeSubscription.unsubscribe();
  }
}
