import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ThemeListComponent } from './components/theme-list/theme-list.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'themes', component: ThemeListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
