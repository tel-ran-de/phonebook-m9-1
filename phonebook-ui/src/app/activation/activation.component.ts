import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-registration-confirmation',
  templateUrl: './activation.component.html',
  styleUrls: ['./activation.component.css']
})
export class ActivationComponent implements OnInit {
  showSpinner = true;
  showSuccess = false;
  errorMessage = '';

  constructor(private userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.sendToken();
  }

  sendToken(): void {
    const token = this.route.snapshot.paramMap.get('token');

    this.userService.activateUser(token)
      .subscribe(
        () => {
          this.showSpinner = false;
          this.showSuccess = true;
        },
        (error) => {
          this.showSpinner = false;
          this.errorMessage = error;
        }
      );
  }
}
