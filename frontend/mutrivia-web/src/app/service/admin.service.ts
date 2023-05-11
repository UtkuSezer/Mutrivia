import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ADMIN_API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(
    private http:HttpClient,
  ) {}
  
  authenticate(id:string, password:string){
    return this.http.get(`${ADMIN_API_URL}/authenticate`);
  }

  generateQuestion(museumId:string, artifactText:string){
    return this.http.get<boolean>(`${ADMIN_API_URL}/generate/${museumId}/${artifactText}`);
  }

}
