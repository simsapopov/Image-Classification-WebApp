import { Component } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ics';
  public isLightTheme:boolean = true;
  public themeIcon:string= "sun";
  public LIGHT_THEME_FILE_PATH:string = "./assets/styles/clr-ui.min.css";
  public DARK_THEME_FILE_PATH:string = "./assets/styles/clr-ui-dark.min.css";
  
  public setTheme(isLightTheme:boolean){
    this.isLightTheme=isLightTheme;
    this.themeIcon=this.isLightTheme?"sun":"moon";
    this.loadTheme(this.isLightTheme)
  }

  public loadTheme(isLightTheme:boolean){
    let themeStyleElement = document.getElementById("theme-css");
    const cssThemeFilePath = isLightTheme?this.LIGHT_THEME_FILE_PATH:this.DARK_THEME_FILE_PATH;
    themeStyleElement?.setAttribute("href", cssThemeFilePath)
  }
}
