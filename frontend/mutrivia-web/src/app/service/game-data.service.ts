import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { API_URL, GAME_API_URL } from 'src/app/app.constants';
import { User } from '../models/user';
import { Question } from '../models/question';
import { LeaderboardRecord } from '../models/leaderboard-record';

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

  checkLeaderboard(userId:string){
    return this.http.get(`${GAME_API_URL}/checkleaderboard/${userId}`);
  }

  getLeaderboard(userId:string){
    return this.http.get<LeaderboardRecord[]>(`${GAME_API_URL}/leaderboard/${userId}`);
  }

  notifyPause(sessionId:string){
    return this.http.get(`${GAME_API_URL}/notifypause/${sessionId}`);
  }

  checkPause(sessionId:string){
    return this.http.get<boolean>(`${GAME_API_URL}/checkpause/${sessionId}`);
  }

  answerQuestion(userId: string){
    return this.http.get(`${GAME_API_URL}/answerquestion/${userId}`);
  }

  getQuestionNumber(museumId: string){
    return this.http.get<number>(`${GAME_API_URL}/sessionindex/${museumId}`);
  }
}
