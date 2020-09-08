import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../service/token-storage.service";
import {Router} from "@angular/router";
import {Contact} from "../model/contact";
import {ContactService} from "../service/contact-service.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  profile: Contact;

  constructor(private contactService: ContactService,
              private tokenStorage: TokenStorageService,
              private router: Router) {
  }

  ngOnInit() {
    this.profile = new Contact();
    this.contactService.getProfile().subscribe(value => this.profile = value);
  }

  logout() {
    this.tokenStorage.signOut();
    this.router.navigate(['../user/login']).then();
  }
}
