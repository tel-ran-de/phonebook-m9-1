import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {RegistrationPendingComponent} from "./registration-pending/registration-pending.component";
import {RegistrationConfirmationComponent} from "./registration-confirmation/registration-confirmation.component";


const routes: Routes = [
  // {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'registration', component: RegistrationComponent},
  {path: 'registration-pending', component: RegistrationPendingComponent},
  {path: 'registration-confirmation', component: RegistrationConfirmationComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
