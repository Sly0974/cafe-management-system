import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  readonly productsUrl: string = environment.apiUrl + "/products"
  readonly defaultHeaders = new HttpHeaders().set("Content-Type", "application/json");

  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.productsUrl, data, {
      headers: this.defaultHeaders
    });
  }

  update(data: any) {
    return this.httpClient.put(this.productsUrl, data, {
      headers: this.defaultHeaders
    });
  }

  getAll() {
    return this.httpClient.get(this.productsUrl);
  }

  delete(id: number) {
    return this.httpClient.delete(this.productsUrl + "/" + id, {
      headers: this.defaultHeaders
    });
  }

}
