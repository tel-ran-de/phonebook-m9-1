import {Component, Input, OnInit} from '@angular/core';
import {AddressService} from "src/app/service/address.service";
import {Address} from "src/app/model/address";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {AddressAddModalComponent} from "../address-add-modal/address-add-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit {

  @Input()
  contactId: number;

  addressesFromServer: Address[] = [];
  addressesToDisplay: Address[] = [];

  searchFormAddress: FormGroup;
  errorMessage: string;
  loading: boolean;

  constructor(private addressService: AddressService,
              private fb: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.loading = true;

    this.searchFormAddress = this.fb.group({
      searchInput: []
    })

    this.searchFormAddress.get("searchInput").valueChanges.subscribe(searchText =>
      this.addressesToDisplay = this.search(searchText))

    this.reloadAddresses();
    this.addressService.trigger$.subscribe(() => this.reloadAddresses());
  }

  reloadAddresses(): void {
    this.addressService.getAllAddressesByContactId(this.contactId)
      .subscribe(addresses => this.callbackOk(addresses), error => this.callbackError(error));
  }

  callbackOk(value: Address[]) {
    this.errorMessage = ''
    this.loading = false
    this.addressesFromServer = value
    this.addressesToDisplay = value;
  }

  callbackError(error: any) {
    this.errorMessage = SubscriptionErrorHandle(error)
    this.loading = false
  }

  search(text: string) {
    return this.addressesFromServer.filter(addressItem => {
      const term = text.toLowerCase()
      const valueToString = addressItem.country + " " + addressItem.city + " " + addressItem.zip + " " + addressItem.street
      return valueToString.toLowerCase().includes(term)
    })
  }

  openModalAddAddress() {
    const modalRef = this.modalService.open(AddressAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }
}
