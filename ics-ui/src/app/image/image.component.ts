import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ImageService } from '../image.service';

interface Image {
  id: number;
  tags: Tag[];
  name: string;
  url: string;
  message?: string;
}

interface Tag {
  id: number;
  tag: string;
  confidencePercentage: number;
}

@Component({
  selector: 'app-result',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
})
export class ResultComponent implements OnInit {
  image: Image | undefined = undefined;
  imageNotFound: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private imageService: ImageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');

      this.http
        .get<Image>(`http://localhost:8079/api/v2/images/${id}`)
        .subscribe(
          (data) => {
            this.image = data;
            this.imageNotFound = false;

            this.http
              .get(`http://localhost:8079/api/v2/message/${id}`, {
                responseType: 'text',
              })
              .subscribe(
                (response) => {
                  if (this.image) {
                    this.image.message = response;
                  }
                },
                (error) => console.error('Error:', error)
              );
          },
          (error) => {
            console.error('Error:', error);
            this.imageNotFound = true;
          }
        );
    });
  }

  replaceTags(): void {
    if (this.image) {
      const id = this.image.id;

      this.http
        .get(`http://localhost:8079/api/v2/replacetags/${id}`, {
          responseType: 'text',
        })
        .subscribe(
          () => {
            this.ngOnInit();
            console.log('da');
          },
          (error) => console.error('Error:', error)
        );
    }
  }
}
