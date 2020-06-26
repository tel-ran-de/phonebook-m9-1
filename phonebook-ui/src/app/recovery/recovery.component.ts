import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../logic-service/user.service";

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.css']
})

export class RecoveryComponent implements OnInit {

  recoveryForm: FormGroup;
  loading: boolean;
  submitted: boolean;
  error: string;

  constructor(private fb: FormBuilder, private router: Router, private userService: UserService) {
    this.createForm();
  }

  createForm() {
    this.recoveryForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.submitted = true;


    this.userService.recovery(this.recoveryForm.value)
      .subscribe(
        data => {

          this.loading = true;
        },
        error => {
          console.log("error connection");
        }
      )

  }
}
