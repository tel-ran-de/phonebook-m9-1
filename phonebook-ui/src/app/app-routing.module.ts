import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {ActivateEmailComponent} from "./activate-email/activate-email.component";
import {ActivationComponent} from "./activation/activation.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {PasswordRecoveryComponent} from "./password-recovery/password-recovery.component";
import {LoginComponent} from "./login/login.component";
import {AuthGuard} from "./service/auth.guard";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {ContactsComponent} from "./pages/contacts/contacts.component";

const routes: Routes = [
  {path: '', redirectTo: 'contacts', pathMatch: 'full'},
  {path: 'user/login', component: LoginComponent},
  {path: 'user/registration', component: RegistrationComponent},
  {path: 'user/activate-email', component: ActivateEmailComponent},
  {path: 'user/activation/:token', component: ActivationComponent},
  {path: 'user/forgot-password', component: ForgotPasswordComponent},
  {path: 'user/password-recovery/:token', component: PasswordRecoveryComponent},
  {
    path: '', component: HomePageComponent,
    children: [
      {
        path: 'contacts', component: ContactsComponent
      }
    ], canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
