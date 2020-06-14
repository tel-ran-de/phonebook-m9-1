import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationPendingComponent } from './registration-pending/registration-pending.component';
import { RegistrationConfirmationComponent } from './registration-confirmation/registration-confirmation.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    RegistrationPendingComponent,
    RegistrationConfirmationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
