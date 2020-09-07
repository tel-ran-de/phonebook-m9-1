import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {Utils} from "../service/utils/utils";

@Component({
  selector: 'app-activation',
  templateUrl: './activation.component.html',
  styleUrls: ['./activation.component.css']
})
export class ActivationComponent implements OnInit, OnDestroy {

  registrationFail: boolean;
  sendRequest: any;
  errorMessage: string;
  private token: string;

  timeLeft: number = 5;

  private utils: Utils;
  private subscription: Subscription;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router) {
    this.utils = new Utils;
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.sendToken()
  }

  sendToken() {
    if (this.token.length < 10) {
      this.sendRequest = true;
      this.registrationFail = true;
      this.errorMessage = 'Your link is not active anymore. Check your confirmation link';
      return;
    }
    this.subscription = this.userService.sendRequestToConfirmRegistration(this.token).subscribe(() => {
        this.sendRequest = true;
        this.registrationFail = false;

        this.startTimer(1, 'login');
      },
      error => {
        this.sendRequest = true;
        this.registrationFail = true;
        this.errorMessage = this.utils.subscribtionErrorHandle(error);

        this.startTimer(2, 'registration');
      });
  }

  private startTimer(koef: number, pathToNavigate: string) {
    this.timeLeft *= koef;
    setInterval(() => {
      if (this.timeLeft > 0)
        this.timeLeft--;
      else
        this.router.navigate(['../../' + pathToNavigate], {relativeTo: this.route}).then()
    }, 1000)
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }
}
