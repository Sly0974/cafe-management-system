import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  private readonly billsUrl:string = environment.apiUrl + "/bills";
  private readonly defaultHeaders:HttpHeaders = new HttpHeaders().set("Content-Type", "application/json");

  constructor(private httpClient: HttpClient) { }

  generateReport(data: any) {
    return this.httpClient.post(this.billsUrl, data, {
      headers: this.defaultHeaders
    })
  }

  getPdf(id: number) {
    return this.httpClient.get(this.billsUrl + "/" + id + "/pdf");
  }

  getBills() {
    return this.httpClient.get(this.billsUrl);
  }
  
}
