import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {Subscription} from 'rxjs';
import {Utils} from '../service/utils/utils';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent implements OnInit, OnDestroy {

  form: FormGroup;

  errorMessage: string;
  userExistMessagae: boolean;
  pageName = 'Password recovery';
  projectName = 'Phone book';
  pageDescription = 'Login or register from here to access.';
  private utils: Utils;

  private subscription: Subscription;

  constructor(private fb: FormBuilder,
              private router: Router,
              private route: ActivatedRoute,
              private userService: UserService,) {
    this.utils = new Utils;
  }

  createForm() {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.pattern("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,10}$")]],
      password: [null, [Validators.minLength(8),
        Validators.required,
        Validators.maxLength(20)]],
      confirm_password: ['', [Validators.required]]
    }, {
      validators: this.utils.confirmedPassValidator('password', 'confirm_password')
    });
  }

  ngOnInit(): void {
    this.createForm();
  }

  onSubmit() {
    this.userService.newUserRegistration(this.form.value)
      .subscribe(
        () => {
          this.router.navigate(['../activate-email'], {relativeTo: this.route}).then();
        },
        error => {
          this.errorMessage = this.utils.subscribtionErrorHandle(error);
          if (this.errorMessage === 'Error! User already exists')
            this.userExistMessagae = true;
        }
      );
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
