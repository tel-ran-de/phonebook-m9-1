import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {first} from "rxjs/operators";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {TokenStorageService} from "../service/tokenHandle/token-storage.service";
import {UserService} from "../service/user.service";
import {SubscriptionErrorHandle} from "../service/subscriptionErrorHandle";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  form: FormGroup;

  pageName = 'Login';
  title = 'Password Recovery';
  projectName = 'Phone book';

  errorMessage: string;
  private headerName = 'Access-Token';

  loading: boolean;
  loginSubscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private tokenStorage: TokenStorageService) {
    this.createForm();
  }

  createForm(): void {
    this.form = this.fb.group({
      email: [null, [Validators.required,
        Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]],
      password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.loginSubscription = this.userService.login(this.form.value)
      .pipe(first())
      .subscribe(
        (data: HttpResponse<any>) => this.callbackOkLogin(data), error => this.callbackErrorLogin(error));
  }

  callbackOkLogin(data: HttpResponse<any>): void {
    this.tokenStorage.signOut();
    this.tokenStorage.saveToken(data.headers.get(this.headerName));

    this.loading = false;
    this.router.navigate(['./contacts']).then();
  }

  callbackErrorLogin(error: HttpErrorResponse): void {
    if (error.status === 401)
      this.errorMessage = 'Please check your activation or Login + Password combination';
    else this.errorMessage = SubscriptionErrorHandle(error);

    if (this.errorMessage)
      this.loading = false;

    this.form.reset();
  }

  ngOnDestroy(): void {
    if (this.loginSubscription)
      this.loginSubscription.unsubscribe();
  }
}
