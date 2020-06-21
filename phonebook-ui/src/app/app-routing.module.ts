import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationComponent} from "./registration/registration.component";
import {PendingComponent} from "./pending/pending.component";
import {ActivationComponent} from "./activation/activation.component";


const routes: Routes = [
  {path: '', redirectTo: '/registration', pathMatch: 'full'},
  {path: 'registration', component: RegistrationComponent},
  {path: 'pending', component: PendingComponent},
  {path: 'activation', component: ActivationComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
