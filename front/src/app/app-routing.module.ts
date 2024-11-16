import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { AuthChoiceComponent } from './components/auth-choice/auth-choice.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ThemeListComponent } from './components/theme-list/theme-list.component';
import { AuthGuard } from './guards/auth.guard';
import { ProfileComponent } from "./components/profile/profile.component";
import {ArticlesComponent} from "./components/articles/articles.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'auth-choice', component: AuthChoiceComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'articles', component: ArticlesComponent, canActivate: [AuthGuard] },
  { path: 'themes', component: ThemeListComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
