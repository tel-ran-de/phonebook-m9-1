import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { Validators } from '@angular/forms';
import {confirmPasswordValidator} from "../directive/confirm-password-validator.directive";
import {ErrorStateMatcher} from '@angular/material/core';
import {UserService} from "../service/user.service";
import {RegistrationUser} from "../model/registration-user.model";
import {Router} from "@angular/router";

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.hasError('differentPasswords') && control.parent.dirty);

    return (invalidCtrl || invalidParent);
  }
}

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[a-zA-Z]{2,10}$'),
      Validators.minLength(6),
      Validators.maxLength(50)
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(10)
    ]),
    confirmPassword: new FormControl(''),
  }, { validators: confirmPasswordValidator });

  confirmPasswordMatcher = new MyErrorStateMatcher();

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
  }

  getEmailErrorMessage() {
    if (this.registrationForm.get('email').hasError('required')) {
      return 'Required field';
    }
    else if (this.registrationForm.get('email').hasError('pattern')) {
      return 'Invalid email';
    }
    else if (this.registrationForm.get('email').hasError('minlength')) {
      return 'Min length is 6 characters';
    }
    else if (this.registrationForm.get('email').hasError('maxlength')) {
      return 'Max length is 50 characters';
    }
    else {
      return '';
    }
  }

  getPasswordErrorMessage() {
    if (this.registrationForm.get('password').hasError('required')) {
      return 'Required field';
    }
    else if (this.registrationForm.get('password').hasError('minlength')) {
      return 'Min length is 5 characters';
    }
    else if (this.registrationForm.get('password').hasError('maxlength')) {
      return 'Max length is 10 characters';
    }
    else {
      return '';
    }
  }

  onSubmit() {
    let user: RegistrationUser = {
      email: this.registrationForm.get('email').value,
      password: this.registrationForm.get('password').value
    };

    this.userService.registerNewUser(user)
      .subscribe(response => {
        this.router.navigate(['user/pending'])//,
          // error => {
          // this.ErrorMessage()
          //}
      });
  }
}
