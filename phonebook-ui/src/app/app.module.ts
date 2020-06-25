import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';

import { LayoutModule } from '@angular/cdk/layout';
import {  MatButtonModule } from  '@angular/material/button';

import {MatCardModule} from '@angular/material/card';
import { MaterialModule } from './material.module';
import { PendingPageComponent } from './pending-page/pending-page.component';
import { LoginComponent } from './login/login.component';
import { NewPasswordPageComponent } from './new-password-page/new-password-page.component';



@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    PendingPageComponent,
    LoginComponent,
    NewPasswordPageComponent,



  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatButtonModule,
    MaterialModule,
    MatCardModule
  ],


  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
