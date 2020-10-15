import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PhoneService} from "src/app/service/phone.service";
import {Phone} from "src/app/model/phone";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";
import {PhoneAddModalComponent} from "../phone-add-modal/add-phone-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.css']
})
export class PhoneComponent implements OnInit, OnDestroy {

  @Input()
  contactId: number;

  searchFormPhone: FormGroup;

  phonesFromServer: Phone[] = [];
  phonesToDisplay: Phone[] = [];

  errorMessage: string;
  loading: boolean;
  private triggerSubscription: Subscription;

  constructor(private phoneService: PhoneService,
              private fb: FormBuilder,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.searchFormPhone = this.fb.group({
      searchInput: []
    })

    this.reloadPhones();

    this.searchFormPhone.get("searchInput").valueChanges.subscribe(searchText => {
      this.phonesToDisplay = this.search(searchText);
    });

    this.triggerSubscription = this.phoneService.trigger$
      .subscribe(() => {
        this.phonesToDisplay = [];
        this.reloadPhones();
      });
  }

  reloadPhones(): void {
    this.loading = true

    this.phoneService.getAllPhonesByContactId(this.contactId)
      .subscribe(phones => this.callbackOkGetAllPhones(phones), error => this.callbackErrorGetAllPhones(error));
  }

  callbackOkGetAllPhones(value: Phone[]) {
    this.errorMessage = ''
    this.loading = false
    this.phonesFromServer = value
    this.phonesToDisplay = value;
  }

  callbackErrorGetAllPhones(error: any) {
    this.errorMessage = SubscriptionErrorHandle(error)
    this.loading = false
  }

  search(text: string) {
    return this.phonesFromServer.filter(value => {
      const term = text.toLowerCase();
      const valueToString = value.countryCode + " " + value.phoneNumber;
      return valueToString.toLowerCase().includes(term)
    })
  }

  openModalAddPhone() {
    const modalRef = this.modalService.open(PhoneAddModalComponent);
    modalRef.componentInstance.contactId = this.contactId;
  }

  ngOnDestroy(): void {
    this.triggerSubscription.unsubscribe();
  }
}
