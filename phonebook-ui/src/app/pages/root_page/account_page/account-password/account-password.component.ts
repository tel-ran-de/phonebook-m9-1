import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../../../service/user.service";
import {ConfirmedValidator} from "../../../../password-recovery/confirmed.validator";
import {HttpErrorResponse} from "@angular/common/http";
import {Subscription} from "rxjs";
import {SubscriptionErrorHandle} from "../../../../service/subscriptionErrorHandle";

@Component({
  selector: 'app-account-password',
  templateUrl: './account-password.component.html',
  styleUrls: ['./account-password.component.css']
})
export class AccountPasswordComponent implements OnInit, OnDestroy {

  loadingSuccess: boolean;
  form: FormGroup;
  loading: boolean;
  errorMessage: string;

  changeSubscription: Subscription;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.createForm();
  }

  createForm(): void {
    this.form = this.fb.group({
      password: [null, [Validators.required, Validators.minLength(8), Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.required]]
    }, {
      validators: ConfirmedValidator('password', 'confirm_password')
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.loadingSuccess = false;
    this.loading = true;
    this.errorMessage = '';

    this.changeSubscription = this.userService.changePassAuthUser(this.form.controls['password'].value)
      .subscribe(() => this.callbackOk(), error => this.callbackError(error));
  }

  callbackOk(): void {
    this.loading = false;
    this.loadingSuccess = true;
  }

  callbackError(error: HttpErrorResponse): void {
    this.loading = false;
    this.errorMessage = SubscriptionErrorHandle(error);

    this.form.reset();
  }

  ngOnDestroy(): void {
    if (this.changeSubscription)
      this.changeSubscription.unsubscribe();
  }
}
