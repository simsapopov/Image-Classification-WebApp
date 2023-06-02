import { Component, OnInit } from '@angular/core';
import { ImageService } from '../image.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';

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
  styleUrls: ['./image-gallery.component.scss'],
})
export class GalleryComponent implements OnInit {
  images: Image[] | undefined;
  tagFormControl = new FormControl();
  inputFormControl = new FormControl();

  constructor(
    private imageService: ImageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.inputFormControl.valueChanges
      .pipe(debounceTime(500))
      .subscribe((tag: string) => {
        this.filterImagesByTag(tag, null);
        this.tagFormControl.setValue(tag, { emitEvent: false });
      });

    this.route.queryParamMap.subscribe((params) => {
      const tag = params.get('tag');
      this.tagFormControl.setValue(tag, { emitEvent: false });
      this.inputFormControl.setValue(tag, { emitEvent: false });
      this.loadImagesByTag(tag);
    });
  }

  loadImagesByTag(tag: string | null): void {
    if (tag) {
      console.log('Searching with Tag' + tag);

      this.imageService.getImageByTag(tag).subscribe(
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
          console.log('All images');
          this.images = images;
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }

  filterImagesByTag(tag: string, event: Event | null): void {
    if (event) {
      event.stopPropagation();
    }
    console.log(tag);
    if (tag) {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: { tag: tag },
        queryParamsHandling: 'merge',
      });
    } else {
      this.router.navigate([], {
        relativeTo: this.route,
      });
    }
  }
}
