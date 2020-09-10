import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../service/user.service';
import {Subscription} from "rxjs";
import {Utils} from '../service/utils/utils';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  form: FormGroup;
  pageName = 'Password recovery';

  projectName = 'Phone book';
  pageDescription = 'Login or register from here to access.';

  loading: boolean;
  errorMessage: string;

  private subscription: Subscription;
  private utils: Utils;
  load: boolean;
  email: string;

  constructor(private fb: FormBuilder,
              private userService: UserService) {
    this.utils = new Utils;
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required,
        Validators.pattern("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,10}$")]]
    });
  }

  ngOnInit(): void {
    this.createForm();
  }

  onSubmit() {
    this.loading = true;
    this.load = false;
    this.errorMessage = '';
    this.email = '';


    this.subscription = this.userService.forgotPassword(this.form.value)
      .subscribe(
        () => {
          this.loading = false;
          this.load = true;
          this.email = this.form.value['email']
        },
        error => {
          this.errorMessage = error.statusText;

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
