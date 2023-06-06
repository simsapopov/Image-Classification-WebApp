import { Component, OnInit } from '@angular/core';
import { ImageService } from '../image.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Image, Tag, ImagePage } from '../image.model';


@Component({
  selector: 'app-gallery',
  templateUrl: './image-gallery.component.html',
  styleUrls: ['./image-gallery.component.scss'],
})
export class GalleryComponent implements OnInit {
  images: Image[] = [];
  tagFormControl = new FormControl();
  inputFormControl = new FormControl();
  isAscending = true;
  sortButtonText = 'Asc By Date';

  page: number = 0;
  totalPages: number = 0;
  size: number = 12;
  filterParams = {
    page: 0,
    size: 12,
    sort: 'asc',
    tag: null,
  };
  constructor(
    private imageService: ImageService,
    private route: ActivatedRoute,
    private router: Router,
    
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

      if (tag) {
        this.loadImagesByTag(tag);
      } else {
        this.loadDefaultImages();
      }
    });
  }

  loadDefaultImages(): void {
    const sort = this.isAscending ? 'asc' : 'desc';
    const page = 0;
    const size = 12;
    const tag = null;

    this.imageService.getImages(page, size, sort, tag).subscribe(
      (page: ImagePage) => {
        this.images = page.content;
        this.totalPages = page.totalPages;
      },
      (error: any) => {
        console.error(error);
      }
    );
  }

  loadPrevious(): void {
    if (this.page > 0) {
      this.page--;
      this.loadImages();
    }
  }

  loadNext(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadImages();
    }
  }

  sortImages(): void {
    this.isAscending = !this.isAscending;
    if (this.isAscending) {
      this.sortButtonText = 'Asc By Date';
    } else {
      this.sortButtonText = 'Desc By Date';
    }
    this.page = 0;

    this.loadImages();
  }

  loadImages(): void {
    const tag = this.tagFormControl.value ? this.tagFormControl.value : null;
    this.imageService
      .getImages(this.page, this.size, this.isAscending ? 'asc' : 'desc', tag)
      .subscribe(
        (page: ImagePage) => {
          this.images = page.content;
          this.totalPages = page.totalPages;
        },
        (error: any) => {
          console.error(error);
        }
      );
  }
  ImagesByTag(tag: string | null): void {
    if (tag) {
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

  public filterImagesByTag(tag: string, event: Event | null): void {
    if (event) {
      event.stopPropagation();
    }

    if (tag) {
      this.page=0;
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: { tag: tag},
        queryParamsHandling: 'merge',
      });
    } else {
      this.router.navigate(['/gallery']);
    }
  }
  loadImagesByTag(tag: string | null): void {
    if (tag) {
      console.log('Searching with Tag: ' + tag);
      this.loadImages();
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
}
