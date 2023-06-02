import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import {ClrFormsModule, ClrButtonModule } from '@clr/angular'
import { AppComponent } from './app.component';
import { ClassifyComponent } from './classify/classify.component';
import { CommonModule } from '@angular/common';
import { ResultComponent } from './image/image.component';
import { GalleryComponent } from './image-gallery/image-gallery.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
@NgModule({schemas: [CUSTOM_ELEMENTS_SCHEMA],
  declarations: [
    AppComponent,
    ClassifyComponent,
    ResultComponent,
    GalleryComponent,
    
  ],
  imports: [
    BrowserModule,
    MatIconModule,
    ClrFormsModule,
    ClrButtonModule,
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
