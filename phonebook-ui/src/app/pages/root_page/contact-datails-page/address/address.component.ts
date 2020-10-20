import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {AddressService} from "src/app/service/address.service";
import {Address} from "src/app/model/address";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {AddressAddModalComponent} from "../address-add-modal/address-add-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  addressesFromServer: Address[] = [];
  addressesToDisplay: Address[] = [];

  searchFormAddress: FormGroup;
  errorMessage: string;
  loading: boolean;

  formSubscription: Subscription;
  getAllSubscription: Subscription;
  triggerSubscription: Subscription;

  constructor(private addressService: AddressService,
              private fb: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.searchFormAddress = this.fb.group({
      searchInput: []
    });

    this.reloadAddresses();

    this.formSubscription = this.searchFormAddress.get("searchInput").valueChanges.subscribe(searchText =>
      this.addressesToDisplay = this.search(searchText));

    this.reloadAddresses();
    this.triggerSubscription = this.addressService.trigger$
      .subscribe(() => {
        this.addressesToDisplay = [];
        this.reloadAddresses();
      });
  }

  reloadAddresses(): void {
    this.loading = true;

    this.getAllSubscription = this.addressService.getAllAddressesByContactId(this.contactId)
      .subscribe(addresses => this.callbackOk(addresses), error => this.callbackError(error));
  }

  callbackOk(value: Address[]): void {
    this.loading = false;

    this.addressesFromServer = value;
    this.addressesToDisplay = value;
  }

  callbackError(error: any): void {
    this.loading = false;

    this.errorMessage = SubscriptionErrorHandle(error);
  }

  search(text: string): Address[] {
    return this.addressesFromServer.filter(addressItem => {
      const term = text.toLowerCase();
      const valueToString = addressItem.country + " " + addressItem.city + " " + addressItem.zip + " " + addressItem.street
      return valueToString.toLowerCase().includes(term)
    });
  }

  openModalAddAddress(): void {
    const modalRef = this.modalService.open(AddressAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  ngOnDestroy(): void {
    if (this.formSubscription)
      this.formSubscription.unsubscribe();
    if (this.getAllSubscription)
      this.getAllSubscription.unsubscribe();
    if (this.triggerSubscription)
      this.triggerSubscription.unsubscribe();
  }
}
