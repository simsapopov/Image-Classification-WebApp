import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private readonly API_ENDPOINT = 'http://localhost:8079/api/v2/classifyimagga';
  private readonly API_ENDPOINT_Clarifai = 'http://localhost:8079/api/v2/classifyclarifai';
  private readonly API_ENDPOINT_GETIMAGE = 'http://localhost:8079/api/v2'
  getImagesByPartialTag: any;

  constructor(private http: HttpClient) { }
  getAllImages(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8079/api/v2/images');
  }
  classifyImage(imageUrl: string): Observable<any> {
    const transformedUrl = this.transformUrl(imageUrl);
    console.log(transformedUrl);
    return this.http.post(this.API_ENDPOINT, { imageUrl: transformedUrl });
  }
  classifyImageImagga(imageUrl: string): Observable<any> {
    const transformedUrl = this.transformUrl(imageUrl);
    console.log(transformedUrl);
    return this.http.post(this.API_ENDPOINT, { imageUrl: transformedUrl });
  }
  classifyImageClarifai(imageUrl: string): Observable<any> {
    const transformedUrl = this.transformUrl(imageUrl);
    console.log(transformedUrl);
    return this.http.post(this.API_ENDPOINT_Clarifai, { imageUrl: transformedUrl });
  }


  getImageByTag(tag: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_ENDPOINT_GETIMAGE}/${tag}`);
  }
  transformUrl(imageUrl: string): string {
    const urlRegex = /(https?:\/\/[^\s]+)/;
    const matches = imageUrl.match(urlRegex);
    if (matches) {
      return matches[0];
    }
    return imageUrl;
  }
  
}
