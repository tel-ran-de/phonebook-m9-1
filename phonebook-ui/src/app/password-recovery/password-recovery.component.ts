import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../service/user.service";
import {ConfirmedValidator} from "./confirmed.validator";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.css']
})
export class PasswordRecoveryComponent implements OnInit {
  title = 'Password Recovery';
  form: FormGroup;
  loading: boolean;
  submitted: boolean = true;
  errorMessage: string;

  constructor(private fb: FormBuilder, private router: Router, private userService: UserService, private route: ActivatedRoute) {
    this.createForm();
  }

  createForm() {
    this.form = this.fb.group({
      password: [null, [Validators.required, Validators.minLength(8),
        Validators.required, Validators.maxLength(20)]],
      confirm_password: [null, [Validators.required]]
    }, {
      validators: ConfirmedValidator('password', 'confirm_password')
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    const token = this.route.snapshot.paramMap.get('token');

    this.loading = true;
    this.errorMessage = '';

    this.userService.resetPassword(this.form.value, token)
      .subscribe(
        data => {
          this.loading = false;
        },
        error => {
          this.errorMessage = this.errorHandle(error);
          if (this.errorMessage)
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
}
