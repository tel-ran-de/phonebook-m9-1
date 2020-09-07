import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {User} from "../model/user";
import {UserService} from "../service/user.service";
import {ContactService} from "../service/contact-service.service";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit, OnDestroy {
  private subscriptionGetUser: Subscription;
  private subscriptionRemove: Subscription;
  public user: User;


  constructor(public contactService: ContactService, public userService: UserService) {
  }

  ngOnInit(): void {
    this.startTimer();
  }

  onClickRemove(id: number) {
    this.subscriptionRemove = this.contactService.removeContact(id);
  }

  ngOnDestroy(): void {
    if (this.subscriptionGetUser)
      this.subscriptionGetUser.unsubscribe();
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }


  timeLeft: number = 60;

  private startTimer() {
    setInterval(() => {
      if (this.timeLeft > 0)
        this.timeLeft--;
    }, 1000)
  }

}
