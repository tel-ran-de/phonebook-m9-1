import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {UserService} from "../service/user.service";
import {ConfirmedValidator} from "../service/confirmed.validator";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";

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
  isSuccess: boolean;
  loading: boolean;
  load: boolean;

  private subscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private route: ActivatedRoute) {
  }

  createForm() {
    this.form = this.fb.group({
      password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]]
    }, {
      validators: ConfirmedValidator('password', "confirm_password")
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.createForm();
  }

  onSubmit() {
    if (this.token.length < 10) {
      this.errorMessage = 'Your link is not active anymore'
      this.tokenInvalid = true;
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.subscription = this.userService.resetPassword(this.form.value.password, this.token)
      .subscribe(
        () => {
          this.isSuccess = true;
          this.loading = false;
          this.load = true;
        },
        error => {
          this.errorMessage = SubscriptionErrorHandle(error);
          this.isSuccess = false;


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
