import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { AuthChoiceComponent } from './components/auth-choice/auth-choice.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ThemesComponent } from './components/themes/themes.component';
import { AuthGuard } from './guards/auth.guard';
import { ProfileComponent } from "./components/profile/profile.component";
import { ArticlesComponent } from "./components/articles/articles.component";
import { CreateArticleComponent } from "./components/articles/create-article/create-article.component";
import { ReadArticleComponent } from "./components/articles/read-article/read-article.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'auth-choice', component: AuthChoiceComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'articles', component: ArticlesComponent, canActivate: [AuthGuard] },
  { path: 'articles/create', component: CreateArticleComponent, canActivate: [AuthGuard] },
  { path: 'articles/:id', component: ReadArticleComponent, canActivate: [AuthGuard] },
  { path: 'themes', component: ThemesComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
