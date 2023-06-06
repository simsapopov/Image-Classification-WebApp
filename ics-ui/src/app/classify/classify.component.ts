import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormControl,
  AbstractControl,
} from '@angular/forms';
import { ImageService } from '../image.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-classify',
  templateUrl: './classify.component.html',
  styleUrls: ['./classify.component.scss'],
})
export class ClassifyComponent implements OnInit {
  classifyForm: FormGroup;
  classifyButtonClicked = false;
  public imageUrlControl: AbstractControl;
  selectedFile: any;
  selectedFileName: string | null = null;

  constructor(
    private fb: FormBuilder,
    private imageService: ImageService,
    private router: Router
  ) {
    this.classifyForm = this.fb.group({
      ['imageUrl']: ['', Validators.required],
      ['service']: ['imagga', Validators.required],
      ['dataType']: ['url', Validators.required],
    });
    this.imageUrlControl = this.classifyForm.controls['imageUrl'];
  }

  ngOnInit(): void {}

  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.selectedFile = fileList[0];
      this.selectedFileName = this.selectedFile.name;
    }
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    this.classifyButtonClicked = true;

    const service = this.classifyForm.value.service;
    const dataType = this.classifyForm.value.dataType;

    if (dataType === 'url' && this.classifyForm.valid) {
      const imageUrl = this.classifyForm.value.imageUrl;
      this.imageService.classifyImage(imageUrl, service).subscribe(
        (response) => this.handleResponse(response),
        (error) => this.handleError(error)
      );
    } else if (dataType === 'file' && this.selectedFile) {
      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        let base64Image = e.target?.result as string;
        this.imageService.classifyImage(base64Image, service).subscribe(
          (response) => this.handleResponse(response),
          (error) => this.handleError(error)
        );
      };
      reader.readAsDataURL(this.selectedFile);
    }
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

  isUrlTypeSelected(): boolean {
    return this.classifyForm.value.dataType === 'url';
  }

  isImageUrlInvalid(): boolean {
    return (
      this.imageUrlControl.invalid &&
      this.classifyButtonClicked &&
      this.isUrlTypeSelected()
    );
  }

  isFileInvalid(): boolean {
    return (
      this.classifyButtonClicked &&
      this.classifyForm.value.dataType === 'file' &&
      !this.selectedFile
    );
  }
}
