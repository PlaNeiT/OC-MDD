import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import {ThemeDTO} from "../../dtos/theme-dto";
@Component({
  selector: 'app-theme-list',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.css']
})
export class ThemesComponent implements OnInit {
  themes: ThemeDTO[] = [];

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.themeService.getThemes().subscribe(
      (data) => this.themes = data,
      (error) => console.error('Error fetching themes', error)
    );
  }
}
