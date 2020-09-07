import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-activate-email',
  templateUrl: './activate-email.component.html',
  styleUrls: ['./activate-email.component.css']
})
export class ActivateEmailComponent implements OnInit {
  timeLeft: number = 5;

  constructor(private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.startTimer();
  }

  private startTimer() {
    setInterval(() => {
      if (this.timeLeft > 0)
        this.timeLeft--;
      else
        this.router.navigate(['../login'], {relativeTo: this.route}).then()
    }, 1000)
  }

}
