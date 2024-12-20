import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';

import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {AuthChoiceComponent} from './components/auth-choice/auth-choice.component';
import {ThemesComponent} from './components/themes/themes.component';
import {NgOptimizedImage} from "@angular/common";
import {HeaderComponent} from './header/header.component';
import {AuthInterceptor} from './interceptors/auth.interceptor';
import {ProfileComponent} from './components/profile/profile.component';
import {ArticlesComponent} from './components/articles/articles.component';
import {CreateArticleComponent} from './components/articles/create-article/create-article.component';
import {ReadArticleComponent} from './components/articles/read-article/read-article.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AuthChoiceComponent,
    LoginComponent,
    RegisterComponent,
    ThemesComponent,
    HeaderComponent,
    ProfileComponent,
    ArticlesComponent,
    CreateArticleComponent,
    ReadArticleComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    NgOptimizedImage,
    HttpClientModule,
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent],
})
export class AppModule {
}
