import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {AppComponent} from './app.component';
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {PasswordRecoveryComponent} from "./password-recovery/password-recovery.component";
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {RegistrationComponent} from './registration/registration.component';
import {ActivateEmailComponent} from './activate-email/activate-email.component';
import {ActivationComponent} from './activation/activation.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {UserService} from "./service/user.service";
import { LoginComponent } from './login/login.component';
import { HomePageComponent } from './home-page/home-page.component';
import {HttpErrorInterceptor} from "./service/errorHandle/http-error.interceptor";
import {TokenInterceptor} from "./service/tokenHandle/token.interceptor";

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
    {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
