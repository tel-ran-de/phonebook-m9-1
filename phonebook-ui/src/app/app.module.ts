import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';
import { PendingComponent } from './pending/pending.component';
import { ActivationComponent } from './activation/activation.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';
import { RecoveryComponent } from './recovery/recovery.component';
import { NewPasswortComponent } from './new-passwort/new-passwort.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    PendingComponent,
    ActivationComponent,
    ConfirmationComponent,
    RecoveryComponent,
    NewPasswortComponent
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
