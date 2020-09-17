import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {TokenStorageService} from "../service/tokenHandle/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  value: any
  title = 'Welcome';

  constructor(private tokenStorageService: TokenStorageService,
              private router: Router) {
  }

  ngOnInit() {
  }

  logout() {
    this.tokenStorageService.signOut();
    this.router.navigate(['../user/login']).then();
  }
}
