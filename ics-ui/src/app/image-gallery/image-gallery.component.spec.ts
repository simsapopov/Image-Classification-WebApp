import { Component, OnInit } from '@angular/core';
import { ImageService } from '../image.service';
import { ActivatedRoute } from '@angular/router';
export interface Image {
  id: number;
  tags: Tag[];
  name: string;
  url: string;
}

export interface Tag {
  id: number;
  tag: string;
  confidencePercentage: number;
}

@Component({
  selector: 'app-gallery',
  templateUrl: './image-gallery.component.html',
  styleUrls: ['./image-gallery.component.scss']
})
export class GalleryComponent implements OnInit {
  images: any[] | undefined;


tag: string | null = null;

constructor(private imageService: ImageService, private route: ActivatedRoute) { }

ngOnInit(): void {
  this.route.paramMap.subscribe(params => {
    this.tag = params.get('tag');
    this.loadImages();
  });
}

loadImages(): void {
  if (this.tag) {
    this.imageService.getImageByTag(this.tag).subscribe(
      (images) => {
        this.images = images;
      },
      (error) => {
        console.error(error);
      }
    );
  } else {
    this.imageService.getAllImages().subscribe(
      (images) => {
        this.images = images;
      },
      (error) => {
        console.error(error);
      }
    );
  }
}
}