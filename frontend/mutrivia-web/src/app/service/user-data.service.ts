import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { API_URL, USER_API_URL } from 'src/app/app.constants';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  constructor(
    private http:HttpClient,
  ) { }

  getUsersInSession(userId:string){
    return this.http.get<User[]>(`${USER_API_URL}/session/${userId}`);
  }

  getUser(userId:string){
    return this.http.get<User>(`${USER_API_URL}/get/${userId}`);
  }

  addUser(username:string){
    return this.http.post<User>(`${USER_API_URL}/add/${username}`, username);
  }

  deleteUser(userId:string){
    return this.http.delete(`${USER_API_URL}/delete/${userId}`);
  }

  addPointsToUser(userId:string, points:number){
    return this.http.put<User>(`${USER_API_URL}/addpoints/${userId}/${points}`, userId);
  }
}
