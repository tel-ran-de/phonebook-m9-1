import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from "rxjs";
import {UserService} from "../service/user.service";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  form: FormGroup;
  pageName = 'Password recovery';
  projectName = 'Phone book';

  loading: boolean;
  errorMessage: string;
  load: boolean;
  email: string;

  forgotPassSubscription: Subscription;

  constructor(private fb: FormBuilder,
              private userService: UserService) {
    this.createForm();
  }

  ngOnInit(): void {
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required,
        Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });
  }

  onSubmit() {
    this.loading = true;
    this.errorMessage = '';
    this.email = '';

    this.forgotPassSubscription = this.userService.forgotPassword(this.form.value.email.toLowerCase())
      .subscribe(() => this.callbackOkFP(), error => this.callbackErrorFP(error));
  }

  callbackOkFP(): void {
    this.loading = false;
    this.load = true;

    this.email = this.form.value['email']
  }

  callbackErrorFP(error: HttpErrorResponse): void {
    this.errorMessage = SubscriptionErrorHandle(error);

    if (this.errorMessage) {
      this.loading = false;
      this.load = true;
    }

    this.form.reset();
  }

  ngOnDestroy(): void {
    if (this.forgotPassSubscription)
      this.forgotPassSubscription.unsubscribe();
  }
}
