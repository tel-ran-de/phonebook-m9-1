import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../service/user.service';
import {UserRecoveryPass} from '../model/userRecoveryPass';
import {Subscription} from 'rxjs';
import {Utils} from '../service/utils/utils';

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.css']
})
export class PasswordRecoveryComponent implements OnInit, OnDestroy {
  form: FormGroup;

  pageName = 'Password recovery';
  title = 'Password Recovery';
  projectName = 'Phone book';
  pageDescription = 'Forgotten password recovery';

  errorMessage: string;
  token: string;
  tokenInvalid: boolean;

  private subscription: Subscription;
  private utils: Utils;
  success: boolean;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private route: ActivatedRoute) {
    this.utils = new Utils;
  }

  createForm() {
    this.form = this.fb.group({
      password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.required]]
    }, {
      validators: this.utils.confirmedPassValidator('password', 'confirm_password')
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

    let userRecoveryPass = new UserRecoveryPass;
    userRecoveryPass.password = this.form.value.password;
    userRecoveryPass.token = this.token;

    this.subscription = this.userService.resetPassword(userRecoveryPass)
      .subscribe(
        () => {
          this.success = true;
        },
        error => {
          this.success = false;
          this.errorMessage = this.utils.subscribtionErrorHandle(error);
        });
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
