import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {AddressService} from "src/app/service/address.service";
import {Address} from "src/app/model/address";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {AddressAddModalComponent} from "../address-add-modal/address-add-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  addressesToDisplay: Address[] = [];
  searchTerm: string;

  searchFormAddress: FormGroup;
  errorMessage: string;
  loading: boolean;

  formSubscription: Subscription;
  getAllSubscription: Subscription;
  triggerSubscription: Subscription;

  constructor(private addressService: AddressService,
              private fb: FormBuilder,
              private modalService: NgbModal,
              private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.createForm();
    this.reloadAddresses();

    this.formSubscription = this.searchFormAddress.get("searchInput")
      .valueChanges
      .subscribe(searchText => this.searchTerm = searchText);

    this.triggerSubscription = this.addressService.trigger$
      .subscribe(() => {
        this.addressesToDisplay = [];
        this.reloadAddresses();
      });
  }

  reloadAddresses(): void {
    this.loading = true;
    this.getAllSubscription = this.addressService.getAllAddressesByContactId(this.contactId)
      .subscribe(addresses => this.callbackOkGetAllAddresses(addresses), error => this.callbackErrorGetAllAddresses(error));
  }

  callbackOkGetAllAddresses(value: Address[]): void {
    this.errorMessage = '';
    this.loading = false;
    this.addressesToDisplay = value;
  }

  callbackErrorGetAllAddresses(error: HttpErrorResponse): void {
    this.loading = false;
    this.errorMessage = this.translateService.instant('PopUpMsg.' + SubscriptionErrorHandle(error));
  }

  openModalAddAddress(): void {
    const modalRef = this.modalService.open(AddressAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  createForm(): void {
    this.searchFormAddress = this.fb.group({
      searchInput: []
    });
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
