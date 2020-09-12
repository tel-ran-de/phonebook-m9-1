import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  value: any

  constructor(private service: UserService) {
  }

  ngOnInit(): void {
    this.service.getUserData().subscribe(value => this.value = value);
  }
}
