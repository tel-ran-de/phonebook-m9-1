import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {UserService} from "../service/user.service";
import {ConfirmedValidator} from "../service/confirmed.validator";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent implements OnInit, OnDestroy {

  form: FormGroup;
  pageName = 'Registration page';
  projectName = 'Phone book';

  errorMessage: string;
  userExistMessage: boolean;
  loading: boolean;

  subscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService) {
    this.createForm();
  }

  ngOnInit(): void {
  }

  createForm(): void {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]],
      password: [null, [Validators.minLength(8),
        Validators.required,
        Validators.maxLength(20)]],
      confirm_password: ['', [Validators.required]]
    }, {
      validators: ConfirmedValidator('password', "confirm_password")
    });
  }

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.userService.newUserRegistration(this.form.value)
      .subscribe(() => this.callbackOkFP(), error => this.callbackErrorFP(error));
  }

  callbackOkFP(): void {
    this.loading = false;
    this.router.navigate(['user/activate-email']).then();
  }

  callbackErrorFP(error: HttpErrorResponse): void {
    this.errorMessage = SubscriptionErrorHandle(error);
    if (this.errorMessage === 'Error! User already exists')
      this.userExistMessage = true;

    if (this.errorMessage)
      this.loading = false;

    this.form.reset();
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
