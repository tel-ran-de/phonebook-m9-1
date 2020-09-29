import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../../service/tokenHandle/token-storage.service";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddContactModalComponent} from "../add-contact-modal/add-contact-modal.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public isMenuCollapsed = true;

  constructor(private tokenStorage: TokenStorageService,
              private router: Router,
              private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  logout() {
    this.isMenuCollapsed = true;
    this.tokenStorage.signOut();
    this.router.navigate(['../user/login']).then();
  }

  openModal() {
    this.isMenuCollapsed = true;
    this.modalService.open(AddContactModalComponent);
  }
}
