import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Email} from "src/app/model/email";
import {EmailService} from "../../../../service/email.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EmailEditModalComponent} from "../email-edit-modal/email-edit-modal.component";
import {ToastService} from "../../../../service/toast.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-email-table',
  templateUrl: './email-table.component.html',
  styleUrls: ['./email-table.component.css']
})
export class EmailTableComponent implements OnInit, OnDestroy {

  @Input()
  sortedEmailsToDisplay: Email[];

  reverseSort: boolean;
  sortBy: string;

  removeSubscription: Subscription;

  constructor(private modalService: NgbModal,
              private emailService: EmailService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string): void {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.sortedEmailsToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)

    if (this.reverseSort)
      this.sortedEmailsToDisplay.reverse();
  }

  onClickRemove(emailId: number): void {
    this.removeSubscription = this.emailService.removeEmail(emailId)
      .subscribe(() => this.callBackOkRemoveEmail(), () => this.callBackErrorRemoveEmail());
  }

  callBackOkRemoveEmail(): void {
    this.toastService.show('email.emailRemoveOk', {
      classname: 'bg-success text-light',
      id: 'pop-up-success-remove-email'
    });

    this.emailService.triggerOnReloadEmailList();
  }

  callBackErrorRemoveEmail(): void {
    this.toastService.show('email.emailRemoveFail', {
      classname: `bg-danger text-light`,
      id: `pop-up-error-remove-email`
    });
  }

  onClickEditEmail(emailToEdit: Email): void {
    const modalRef = this.modalService.open(EmailEditModalComponent);
    modalRef.componentInstance.emailToEdit = emailToEdit;
  }

  ngOnDestroy(): void {
    if (this.removeSubscription)
      this.removeSubscription.unsubscribe();
  }
}
