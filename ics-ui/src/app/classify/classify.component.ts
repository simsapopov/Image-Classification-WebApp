import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ImageService } from '../image.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-classify',
  templateUrl: './classify.component.html',
  styleUrls: ['./classify.component.scss'],
})
export class ClassifyComponent implements OnInit {
  classifyForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private imageService: ImageService,
    private router: Router
  ) {
    this.classifyForm = this.fb.group({
      imageUrl: ['', Validators.required],
      service: ['imagga', Validators.required],
    });
  }

  ngOnInit(): void {}

  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      let file = fileList[0];
      var reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        let base64Image = e.target?.result as string;

        const service = this.classifyForm.value.service;

        if (service === 'imagga') {
          this.imageService.classifyImageImagga(base64Image).subscribe(
            (response) => this.handleResponse(response),
            (error) => this.handleError(error)
          );
        } else if (service === 'clarifai') {
          this.imageService.classifyImageClarifai(base64Image).subscribe(
            (response) => this.handleResponse(response),
            (error) => this.handleError(error)
          );
        }
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.classifyForm.valid) {
      const imageUrl = this.classifyForm.value.imageUrl;
      const service = this.classifyForm.value.service;

      if (service === 'imagga') {
        this.imageService.classifyImageImagga(imageUrl).subscribe(
          (response) => this.handleResponse(response),
          (error) => this.handleError(error)
        );
      } else if (service === 'clarifai') {
        this.imageService.classifyImageClarifai(imageUrl).subscribe(
          (response) => this.handleResponse(response),
          (error) => this.handleError(error)
        );
      }
    }
  }

  onServiceChanged(event: Event): void {
    console.log(this.classifyForm.value.service);
  }

  handleResponse(response: any) {
    console.log(response);
    if (response) {
      this.router.navigate(['/image', response]);
    } else {
      console.error('Invalid response or response ID');
    }
  }

  handleError(error: any) {
    console.error(error);
  }
}
