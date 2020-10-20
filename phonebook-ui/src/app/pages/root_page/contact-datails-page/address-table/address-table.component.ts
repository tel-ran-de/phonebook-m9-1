import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Address} from "src/app/model/address";
import {AddressService} from "../../../../service/address.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddressEditModalComponent} from "../address-edit-modal/address-edit-modal.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-address-table',
  templateUrl: './address-table.component.html',
  styleUrls: ['./address-table.component.css']
})
export class AddressTableComponent implements OnInit, OnDestroy {

  @Input()
  sortedAddressesToDisplay: Address[];

  reverseSort: boolean;
  sortBy: string;

  private subscription: Subscription;

  constructor(private modalService: NgbModal,
              private addressService: AddressService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string): void {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.sortedAddressesToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)

    if (this.reverseSort)
      this.sortedAddressesToDisplay.reverse();
  }

  onClickRemove(addressId: number): void {
    this.subscription = this.addressService.removeAddress(addressId)
      .subscribe(() => this.callBackOkAddressEdit());
  }

  callBackOkAddressEdit(): void {
    this.addressService.triggerOnReloadAddressesList();
  }

  onClickEdit(addressToEdit: Address): void {
    const modalRef = this.modalService.open(AddressEditModalComponent);
    modalRef.componentInstance.addressToEdit = addressToEdit;
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
