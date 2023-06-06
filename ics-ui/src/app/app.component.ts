import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'ics';
  public isLightTheme: boolean = true;
  public themeIcon: string = 'sun';
  public LIGHT_THEME_FILE_PATH: string = './assets/styles/clr-ui.min.css';
  public DARK_THEME_FILE_PATH: string = './assets/styles/clr-ui-dark.min.css';

  ngOnInit() {
    this.loadThemeFromCache();
  }

  public setTheme(isLightTheme: boolean) {
    this.isLightTheme = isLightTheme;
    this.themeIcon = this.isLightTheme ? 'sun' : 'moon';
    this.loadTheme(this.isLightTheme);
    localStorage.setItem('isLightTheme', JSON.stringify(this.isLightTheme));
  }

  public loadTheme(isLightTheme: boolean) {
    let themeStyleElement = document.getElementById('theme-css');
    const cssThemeFilePath = isLightTheme
      ? this.LIGHT_THEME_FILE_PATH
      : this.DARK_THEME_FILE_PATH;
    themeStyleElement?.setAttribute('href', cssThemeFilePath);
  }

  public loadThemeFromCache() {
    const cachedTheme = JSON.parse(
      localStorage.getItem('isLightTheme') || 'true'
    );
    this.setTheme(cachedTheme);
  }
}
