import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../../../service/user.service";
import {ConfirmedValidator} from "../../../../password-recovery/confirmed.validator";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-account-password',
  templateUrl: './account-password.component.html',
  styleUrls: ['./account-password.component.css']
})
export class AccountPasswordComponent implements OnInit, OnDestroy {
  constructor(private fb: FormBuilder, private userService: UserService) {
    this.createForm();
  }

  loadingSuccess: boolean;
  form: FormGroup;
  loading: boolean;
  errorMessage: string;


  createForm() {
    this.form = this.fb.group({
      password: [null, [Validators.required, Validators.minLength(8), Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.required]]
    }, {
      validators: ConfirmedValidator('password', 'confirm_password')
    });
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }


  onSubmit() {
    this.loadingSuccess = false;
    this.loading = true;
    this.errorMessage = '';


    this.userService.changePassAuthUser(this.form.controls['password'].value).subscribe(
      () => {
        this.loading = false;
        this.loadingSuccess = true;
      }
      ,
      error => {
        this.form.reset();
        this.errorMessage = this.errorHandle(error);
        this.loading = false;
      });
  }

  private errorHandle(error: HttpErrorResponse): string {
    let errorMessage: string;
    if (error.error instanceof ErrorEvent)
      return 'No internet connection';
    else errorMessage = error.error.message;

    if (errorMessage === null || !errorMessage)
      errorMessage = 'Error code: ' + error.status
        + '. If you have this error again, please contact us: support@phone-book.com'
    return errorMessage;
  }

  onClickOk() {
    this.loadingSuccess = false
    this.form.reset();
  }
}
