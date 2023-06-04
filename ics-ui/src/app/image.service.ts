import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private readonly API_ENDPOINT = 'http://localhost:8079/api/v2/classify';
  private readonly API_ENDPOINT_GETIMAGE = 'http://localhost:8079/api/v2';
  
  getImagesByPartialTag: any;

  constructor(private http: HttpClient) {}
  getAllImages(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8079/api/v2/images');
  }
  getImages(page: number, size: number, sort: string, tag: string | null): Observable<any> {
    let params = new HttpParams();
    params = params.append('pageNo', page.toString());
    params = params.append('pageSize', size.toString());
    params = params.append('direction', sort);
    if(tag) {
      params = params.append('tag', tag);
    }
    return this.http.get<any>(this.API_ENDPOINT_GETIMAGE + "/all", { params: params });
  }
  
  classifyImage(imageUrl: string, servicee: string): Observable<any> {
    console.log(imageUrl);

    return this.http.post(this.API_ENDPOINT + '/' + servicee, { imageUrl });
  }
  classifyImageImagga(imageUrl: string): Observable<any> {
    const transformedUrl = this.transformUrl(imageUrl);
    console.log(transformedUrl);
    return this.http.post(this.API_ENDPOINT, { imageUrl: transformedUrl });
  }
  getAllImagesPage(page: number, size: number, sortDirection: string) {
    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
        .set('sortDirection', sortDirection);
    return this.http.get('http://localhost:8080/images/all', { params: params });
}

  getImageByTag(tag: string): Observable<any[]> {
    console.log(tag);
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
