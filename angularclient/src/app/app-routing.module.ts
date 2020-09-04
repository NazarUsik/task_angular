import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {UserListComponent} from './user-list/user-list.component';
import {UserFormComponent} from './user-form/user-form.component';
import {LoginFormComponent} from './login-form/login-form.component';

const routes: Routes = [
  {path: '', component: UserListComponent},
  {path: 'add', component: UserFormComponent},
  {path: 'edit', component: UserFormComponent},
  {path: 'login', component: LoginFormComponent},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
