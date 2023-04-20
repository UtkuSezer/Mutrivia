import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Question } from '../models/question';
import { User } from '../models/user';
import { GameDataService } from '../service/game-data.service';
import { UserDataService } from '../service/user-data.service';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';

@Component({
  selector: 'app-game-view-solo',
  templateUrl: './game-view-solo.component.html',
  styleUrls: ['./game-view-solo.component.css']
})
export class GameViewSoloComponent implements OnInit {

  myUser!: User
  currentQuestion!: Question

  stompClient: any;
  webSocketEndPoint: string = 'http://localhost:8080/ws';
  questionTopic: string = "/topic/question/";

  timeLeft: number = 60;
  interval !: any;
  
  constructor(private userDataService: UserDataService,
    private gameDataService: GameDataService,
    private router: Router) { }

  ngOnInit(): void {
    this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.myUser = data
        this.setTopics()
        this.connect()
        this.onClickGenerateQuestion()
      }
    )
  }

  setTopics(){
    this.questionTopic = this.questionTopic + this.myUser.sessionId;
  }

  onClickGenerateQuestion(){
    this.gameDataService.generateQuestion(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.currentQuestion = data
      }
    )
  }

  onClickOption(i:number){
    this.pauseTimer()
    if(this.currentQuestion.correctChoiceIndex == i){
      console.log("CORRECT");
      this.myUser.score = this.myUser.score + this.timeLeft*10
      //Save the score to database at the end
    }
    else{
      console.log("FALSE");
    }
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        console.log("TIME IS UP")
      }
    },1000)
  }

  pauseTimer() {
    clearInterval(this.interval);
  }

  onClickLeaveGame(){
    this.gameDataService.leaveSession(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        sessionStorage.removeItem("isSolo");
        this.router.navigate(['menu']);
      }
    )
  }

    //WebSocket ------------------------------------------------------------------------------

    connect(): void {
      console.log('WebSocket Connection');
      const ws = new SockJS(this.webSocketEndPoint);
      this.stompClient = Stomp.over(ws);
      const _this = this;
      _this.stompClient.connect({}, function(frame:any) {
        _this.stompClient.subscribe(_this.questionTopic, function(sdkEvent:any) {
          _this.onQuestionMessageReceived(sdkEvent);
        });
      }, this.errorCallBack);
    }
  
    disconnect(): void {
      if (this.stompClient !== null) {
        this.stompClient.disconnect();
      }
      console.log('Disconnected');
    }
  
     // on error, schedule a reconnection attempt
     errorCallBack(error:any) {
      console.log('errorCallBack -> ' + error);
      setTimeout(() => {
          this.connect();
      }, 5000);
    }
  
    onQuestionMessageReceived(message: any) {
      let question: Question = JSON.parse(message.body);
      this.currentQuestion = question;
      this.timeLeft = 60;
      this.startTimer;
    }
    //End of WebSocket ---------------------------------------------------------------------

}
