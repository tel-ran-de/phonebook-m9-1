import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {Utils} from '../service/utils/utils';
import {Subscription} from 'rxjs';
import {TokenStorageService} from "../service/token-storage.service";
import {first} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";

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
  pageDescription = 'login to to continue';

  isLoggedIn = false;
  errorMessage: string;
  private headerName = 'Access-Token';

  loading: boolean;
  private subscription: Subscription;
  private utils: Utils;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private tokenStorage: TokenStorageService) {
    this.utils = new Utils;
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,10}$")]],
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
          this.router.navigate(['../home']).then();
        },
        error => {
          if (error.status === 401)
            this.errorMessage = error.statusText + '\nPlease check your activation or Login + Password combination';
          else this.errorMessage = this.utils.subscriptionErrorHandle(error);

          if (this.errorMessage)
            this.loading = false;
        });
  }

}
