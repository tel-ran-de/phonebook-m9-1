import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {AppComponent} from './app.component';
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {PasswordRecoveryComponent} from "./password-recovery/password-recovery.component";
import {RegistrationComponent} from './registration/registration.component';
import {ActivateEmailComponent} from './activate-email/activate-email.component';
import {ActivationComponent} from './activation/activation.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {UserService} from "./service/user.service";
import {LoginComponent} from './login/login.component';
import {TokenInterceptor} from "./service/tokenHandle/token.interceptor";
import {HttpError401Interceptor} from "./service/errorHandle/http-error401-interceptor.service";
import {ContactsComponent} from './pages/contacts/contacts.component';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {HeaderComponent} from "./pages/header/header.component";
import {ContactItemComponent} from './pages/contact-item/contact-item.component';
import {FooterComponent} from "./pages/footer/footer.component";

@NgModule({
  declarations: [
    AppComponent,
    ForgotPasswordComponent,
    PasswordRecoveryComponent,
    HeaderComponent,
    FooterComponent,
    RegistrationComponent,
    ActivateEmailComponent,
    ActivationComponent,
    LoginComponent,
    HomePageComponent,
    ContactsComponent,
    ContactItemComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
  ],
  providers: [UserService,
    {provide: HTTP_INTERCEPTORS, useClass: HttpError401Interceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
