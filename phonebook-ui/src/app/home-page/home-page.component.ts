import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {TokenStorageService} from "../service/tokenHandle/token-storage.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  value: any
  title = 'Welcome';

  constructor() {
  }

  ngOnInit(): void {
  }
}
