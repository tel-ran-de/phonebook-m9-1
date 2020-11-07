import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../../service/tokenHandle/token-storage.service";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddContactModalComponent} from "../add-contact-modal/add-contact-modal.component";
import {ToastService} from "../../../service/toast.service";
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public isMenuCollapsed = true;

  langs = [
    {key: "en", value: "English"},
    {key: "ru", value: "Russian"},
    {key: "de", value: "German"},
    {key: "uk", value: "Ukraine"}
  ];

  constructor(private tokenStorage: TokenStorageService,
              private router: Router,
              private modalService: NgbModal,
              private toastService: ToastService,
              public translate: TranslateService) {

    translate.addLangs(['en', 'ru', 'de', 'ua']);
    translate.setDefaultLang('en');

    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|ru|de/) ? browserLang : 'en');
  }

  ngOnInit() {
  }

  logout() {
    this.isMenuCollapsed = true;
    this.tokenStorage.signOut();
    this.router.navigate(['../user/login']).then();
    this.toastService.clearToasts();
  }

  openModal() {
    this.isMenuCollapsed = true;
    this.modalService.open(AddContactModalComponent);
  }
}
