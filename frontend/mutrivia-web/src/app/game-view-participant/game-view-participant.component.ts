import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import SockJS from 'sockjs-client';
import { Question } from '../models/question';
import { User } from '../models/user';
import { GameDataService } from '../service/game-data.service';
import { UserDataService } from '../service/user-data.service';
import * as Stomp from 'stompjs';
import { browserRefresh } from '../app.component';

declare const copytext: any;

@Component({
  selector: 'app-game-view-participant',
  templateUrl: './game-view-participant.component.html',
  styleUrls: ['./game-view-participant.component.css']
})
export class GameViewParticipantComponent implements OnInit {

  users: User[] = []
  isGameStarted: boolean = false
  isGamePaused: boolean = false
  myUser!: User
  currentQuestion!: Question
  isAnswerCorrect: boolean = false

  stompClient: any;
  webSocketEndPoint: string = 'http://www.mutrivia.com/api/ws';
  questionTopic: string = "/topic/question/";
  newUserTopic: string = "/topic/newuser/";
  deleteUserTopic: string = "/topic/deleteuser/";
  deleteSessionTopic: string = "/topic/deletesession/";
  startSessionTopic: string = "/topic/startsession/";
  changeQuestionTopic: string = "/topic/changequestion/";

  timeLeft: number = 30;
  interval !: any;
  loaderEndQuiz = false;
  timerRatioString: string = "100%";
  clicked = false;

  constructor(private userDataService: UserDataService,
    private gameDataService: GameDataService,
    private router: Router) { }

  callCopyText(){
    copytext();
  }

  ngOnInit(): void {
    if(browserRefresh){
      this.onClickLeaveSession();
    }

    this.pauseTimer();
    this.resetTimer();
    this.setUsers()
    this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.myUser = data;
        this.setTopics();
        this.connect();
      }
    )
  }

  setUsers() {
    this.userDataService.getUsersInSession(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.users = data;
        this.users.sort((firstUser, secondUser) => secondUser.score - firstUser.score);
      }
    )
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
        this.timerRatioString = ((this.timeLeft / 30) * 100).toString() + "%";
      } else {
        this.pauseTimer();
        this.setUsers();
        this.gameDataService.notifyPause(this.myUser.sessionId as string).subscribe(
          data => {
            this.isGamePaused = true
          }
        )
      }
    },1000)
  }

  pauseTimer() {
    clearInterval(this.interval);
  }

  resetTimer(){
    this.timeLeft = 30;
  }

  onClickLeaveSession(){
    this.loaderEndQuiz = true;
    this.pauseTimer();
    this.gameDataService.leaveSession(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.isGameStarted = false;
        sessionStorage.removeItem("isParticipant");
        this.loaderEndQuiz = false;
        this.router.navigate(['menu'])
      }
    )
  }

  onSessionEndedByHost(){
    this.isGameStarted = false;
    this.router.navigate(['gameover'])
  }

  onClickOption(i:number){
    document.getElementById(i.toString())!.style.backgroundColor = "#ffb74d";
    document.getElementById(i.toString())!.style.color = "white";
    this.clicked = true;
    if(this.currentQuestion.correctChoiceIndex == i){
      this.userDataService.addPointsToUser(this.myUser.userId, this.timeLeft*10).subscribe(
        data=>{
          console.log("CORRECT, POINTS: ", this.timeLeft*10 );
          this.isAnswerCorrect = true;
          this.gameDataService.answerQuestion(this.myUser.userId as string).subscribe(
            data=>{}
          )
        }
      )
    }
    else{
      console.log("FALSE");
      this.isAnswerCorrect = false;
      this.gameDataService.answerQuestion(this.myUser.userId as string).subscribe(
        data=>{}
      )
    }
  }

  setTopics(){
    this.questionTopic = this.questionTopic + this.myUser.userId;
    this.newUserTopic = this.newUserTopic + this.myUser.sessionId;
    this.deleteUserTopic = this.deleteUserTopic + this.myUser.sessionId;
    this.deleteSessionTopic = this.deleteSessionTopic + this.myUser.sessionId;
    this.startSessionTopic = this.startSessionTopic + this.myUser.sessionId;
    this.changeQuestionTopic = this.changeQuestionTopic + this.myUser.sessionId;
  }

  //WebSocket ------------------------------------------------------------------------------

  connect(): void {
    console.log('WebSocket Connection');
    const ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function(frame:any) {
      _this.stompClient.subscribe(_this.newUserTopic, function(sdkEvent:any) {
        _this.onNewUserMessageReceived(sdkEvent);
      });
      _this.stompClient.subscribe(_this.deleteUserTopic, function(sdkEvent:any) {
        _this.onDeleteUserMessageReceived(sdkEvent);
      });
      _this.stompClient.subscribe(_this.questionTopic, function(sdkEvent:any) {
        _this.onQuestionMessageReceived(sdkEvent);
      });
      _this.stompClient.subscribe(_this.deleteSessionTopic, function(sdkEvent:any) {
        _this.onDeleteSessionMessageReceived(sdkEvent);
      });
      _this.stompClient.subscribe(_this.startSessionTopic, function(sdkEvent:any) {
        _this.onStartSessionMessageReceived(sdkEvent);
      });
      _this.stompClient.subscribe(_this.changeQuestionTopic, function(sdkEvent:any) {
        _this.onChangeQuestionMessageReceived(sdkEvent);
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
    this.clicked = false;
    this.pauseTimer();
    let question: Question = JSON.parse(message.body);
    this.currentQuestion = question;
    this.isAnswerCorrect = false;
    this.resetTimer();
    this.isGamePaused = false
    this.startTimer()
  }
  onNewUserMessageReceived(message: any) {
    let user: User = JSON.parse(message.body);
    console.log("User joined with username: " + user.username);
    this.users.unshift(user)
    if(user.userId == this.myUser.userId){
      this.myUser = user;
    }
  }
  onDeleteUserMessageReceived(message: any) {
    let userId: string = (message.body as string);
    this.users = this.users.filter(x => x.userId !== userId);
  }
  onDeleteSessionMessageReceived(message: any) {
    let sessionId: string = (message.body as string);
    if(sessionId === this.myUser.sessionId){
      this.onSessionEndedByHost();
    }
  }
  onStartSessionMessageReceived(message: any) {
    this.isGameStarted = true
  }
  onChangeQuestionMessageReceived(message: any){
    console.log("CHANGE QUESTION")
    this.timeLeft = 0;
  }
  //End of WebSocket ---------------------------------------------------------------------

}
