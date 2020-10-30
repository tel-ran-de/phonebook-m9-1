import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PhoneService} from "src/app/service/phone.service";
import {Phone} from "src/app/model/phone";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {PhoneAddModalComponent} from "../phone-add-modal/add-phone-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  phonesToDisplay: Phone[] = [];
  searchTerm: string;

  searchFormPhone: FormGroup;
  errorMessage: string;
  loading: boolean;

  formSubscription: Subscription;
  getAllSubscription: Subscription;
  triggerSubscription: Subscription;

  constructor(private phoneService: PhoneService,
              private fb: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.createForm();
    this.reloadPhones();

    this.searchFormPhone.get("searchInput")
      .valueChanges
      .subscribe(searchText => this.searchTerm = searchText);

    this.formSubscription = this.triggerSubscription = this.phoneService.trigger$
      .subscribe(() => {
        this.phonesToDisplay = [];
        this.reloadPhones();
      });
  }

  reloadPhones(): void {
    this.loading = true;
    this.getAllSubscription = this.phoneService.getAllPhonesByContactId(this.contactId)
      .subscribe(phones => this.callbackOkGetAllPhones(phones), error => this.callbackErrorGetAllPhones(error));
  }

  callbackOkGetAllPhones(value: Phone[]): void {
    this.errorMessage = '';
    this.loading = false;
    this.phonesToDisplay = value;
  }

  callbackErrorGetAllPhones(error: HttpErrorResponse) {
    this.errorMessage = SubscriptionErrorHandle(error);
    this.loading = false;
  }

  openModalAddPhone(): void {
    const modalRef = this.modalService.open(PhoneAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  createForm(): void {
    this.searchFormPhone = this.fb.group({
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
