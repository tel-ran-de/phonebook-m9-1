import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {ActivationComponent} from "./activation/activation.component";
import {PendingPageComponent} from "./pending-page/pending-page.component";
import {LoginComponent} from "./login/login.component";
import {RecoveryComponent} from "./recovery/recovery.component";
import {NewPasswordPageComponent} from "./new-password-page/new-password-page.component";



const routes: Routes = [


  {path: 'user/registration', component: RegistrationComponent },
  {path: 'user/activation', component: ActivationComponent},
  {path: 'user/pending-page', component: PendingPageComponent},
  {path: 'user/login', component: LoginComponent},
  {path: 'user/recovery', component: RecoveryComponent},
  {path: 'user/new-password-page', component:NewPasswordPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
