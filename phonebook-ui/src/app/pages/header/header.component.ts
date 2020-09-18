import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../service/tokenHandle/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private tokenStorage: TokenStorageService,
              private router: Router) {
  }

  ngOnInit() {
  }

  logout() {
    this.tokenStorage.signOut();
    this.router.navigate(['../user/login']).then();
  }
}