import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {UserService} from "../service/user.service";
import {ConfirmedValidator} from "../service/confirmed.validator";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.css']
})
export class PasswordRecoveryComponent implements OnInit, OnDestroy {

  form: FormGroup;
  pageName = 'Password recovery';
  projectName = 'Phone book';

  errorMessage: string;
  token: string;

  tokenInvalid: boolean;
  loading: boolean;
  load: boolean;

  resetPassSubscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private route: ActivatedRoute) {
    this.createForm();
  }


  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
  }


  createForm(): void {
    this.form = this.fb.group({
      password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]]
    }, {
      validators: ConfirmedValidator('password', "confirm_password")
    });
  }

  onSubmit() {
    if (this.token.length < 10) {
      this.errorMessage = 'Your link is not active anymore'
      this.tokenInvalid = true;
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.resetPassSubscription = this.userService.resetPassword(this.form.value.password, this.token)
      .subscribe(() => this.callbackOkRP(), error => this.callbackErrorRP(error));
  }

  callbackOkRP(): void {
    this.loading = false;
    this.load = true;
  }

  callbackErrorRP(error: HttpErrorResponse): void {
    this.errorMessage = SubscriptionErrorHandle(error);

    if (this.errorMessage) {
      this.loading = false;
      this.load = true;
    }

    this.form.reset();
  }

  ngOnDestroy(): void {
    if (this.resetPassSubscription)
      this.resetPassSubscription.unsubscribe();
  }
}
