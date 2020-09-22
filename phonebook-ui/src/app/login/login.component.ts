import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpResponse} from "@angular/common/http";
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

  isLoggedIn = false;
  errorMessage: string;
  private headerName = 'Access-Token';

  loading: boolean;
  private subscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private tokenStorage: TokenStorageService) {
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required,
        Validators.pattern("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")]],
      password: [null, [Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]]
    });
  }

  ngOnInit(): void {
    this.createForm();

    if (this.tokenStorage.getToken())
      this.isLoggedIn = true;
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }

  onSubmit() {
    this.loading = true;
    this.errorMessage = '';

    this.userService.login(this.form.value).pipe(first())
      .subscribe(
        (data: HttpResponse<any>) => {
          this.tokenStorage.signOut();
          this.tokenStorage.saveToken(data.headers.get(this.headerName));
          this.loading = false;
          this.router.navigate(['./contacts']).then();
        },
        error => {
          if (error.status === 401)
            this.errorMessage = 'Please check your activation or Login + Password combination';
          else this.errorMessage = SubscriptionErrorHandle(error);

          if (this.errorMessage)
            this.loading = false;
        });
  }
}
