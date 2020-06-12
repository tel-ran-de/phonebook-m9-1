import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {ActivationComponent} from "./activation/activation.component";
import {PendingComponent} from "./pending/pending.component";
import {ConfirmationComponent} from "./confirmation/confirmation.component";
import {NewPasswortComponent} from "./new-passwort/new-passwort.component";
import {RecoveryComponent} from "./recovery/recovery.component";


const routes: Routes = [

  {path: 'user/activation', component: ActivationComponent },
  {path: 'user/confirmation', component: ConfirmationComponent },
  {path: 'user/newpasswort', component: NewPasswortComponent },
  {path: 'user/registration', component: RegistrationComponent },
  {path: 'user/recovery', component: RecoveryComponent },
  {path: 'user/pending', component: PendingComponent },






];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
