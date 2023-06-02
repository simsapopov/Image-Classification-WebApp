import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClassifyComponent } from './classify/classify.component';
import { ResultComponent } from './image/image.component';
import { GalleryComponent } from './image-gallery/image-gallery.component';
import '@cds/core/icon/register.js';
import { ClarityIcons, moonIcon } from '@cds/core/icon';

ClarityIcons.addIcons(moonIcon);

const routes: Routes = [
  { path: 'classify', component: ClassifyComponent },
  { path: '', redirectTo: '/classify', pathMatch: 'full' },
  { path: 'image/:id', component: ResultComponent },
  { path: 'image', component: ResultComponent },
  { path: 'gallery', component: GalleryComponent },
  { path: 'image/:id', component: ResultComponent },
  { path: 'gallery/:tag', component: GalleryComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
