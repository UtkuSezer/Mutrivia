import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { API_URL, GAME_API_URL } from 'src/app/app.constants';
import { User } from '../models/user';
import { Question } from '../models/question';

@Injectable({
  providedIn: 'root'
})
export class GameDataService {

  constructor(
    private http:HttpClient,
  ) { }

  generateQuestion(userId:string){
    return this.http.get<Question>(`${GAME_API_URL}/question/${userId}`);
  }

  startSession(userId:string){
    return this.http.get(`${GAME_API_URL}/start/${userId}`);
  }

  hostSession(userId:string, museumId:string){
    return this.http.put<User>(`${GAME_API_URL}/host/${userId}/${museumId}`, userId);
  }

  joinSession(sessionId:string, userId:string){
    return this.http.put<User>(`${GAME_API_URL}/join/${sessionId}/${userId}`, userId);
  }

  soloSession(userId:string, museumId:string){
    return this.http.put<User>(`${GAME_API_URL}/solo/${userId}/${museumId}`, userId);
  }

  leaveSession(userId:string){
    return this.http.get(`${GAME_API_URL}/leave/${userId}`);
  }

  switchToResults(userId:string){
    return this.http.get(`${GAME_API_URL}/results/${userId}`);
  }

  endSession(userId:string){
    return this.http.get(`${GAME_API_URL}/end/${userId}`);
  }

}
