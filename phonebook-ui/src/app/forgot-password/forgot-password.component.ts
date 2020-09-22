import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from "rxjs";
import {UserService} from "../service/user.service";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";

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

  private subscription: Subscription;

  constructor(private fb: FormBuilder,
              private userService: UserService) {
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required,
        Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]]
    });
  }

  ngOnInit(): void {
    this.createForm();
  }

  onSubmit() {
    this.loading = true;
    this.errorMessage = '';
    this.email = '';

    this.subscription = this.userService.forgotPassword(this.form.value.email.toLowerCase())
      .subscribe(
        () => {
          this.loading = false;
          this.load = true;
          this.email = this.form.value['email']
        },
        error => {
          this.errorMessage = SubscriptionErrorHandle(error);

          if (this.errorMessage) {
            this.loading = false;
            this.load = true;
          }
        });
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
