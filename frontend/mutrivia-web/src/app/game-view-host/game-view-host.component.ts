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
  selector: 'app-game-view-host',
  templateUrl: './game-view-host.component.html',
  styleUrls: ['./game-view-host.component.css']
})
export class GameViewHostComponent implements OnInit {

  users: User[] = []
  isGameStarted: boolean = false
  isGamePaused: boolean = false
  myUser!: User
  currentQuestion!: Question
  isAnswerCorrect: boolean = false

  stompClient: any;
  webSocketEndPoint: string = 'http://localhost:8080/ws';
  questionTopic: string = "/topic/question/";
  newUserTopic: string = "/topic/newuser/";
  deleteUserTopic: string = "/topic/deleteuser/";

  timeLeft: number = 30;
  interval !: any;
  pauseInterval !: any;
  pauseTimeLeft: number = 10;
  loaderStartQuiz = false;
  timerRatioString: string = "100%";
  clicked = false;

  constructor(private gameDataService: GameDataService,
    private userDataService: UserDataService,
    private router: Router) { }

  callCopyText(){
    copytext();
  }

  ngOnInit(): void {
    if(browserRefresh){
      this.onClickEndSession();
    }

    this.pauseTimer();
    this.pausePauseTimer();
    this.resetTimer();
    this.resetPauseTimer();
    this.setUsers()
    this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.myUser = data
        this.setTopics()
        this.connect()
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

  setTopics(){
    this.questionTopic = this.questionTopic + this.myUser.userId;
    this.newUserTopic = this.newUserTopic + this.myUser.sessionId;
    this.deleteUserTopic = this.deleteUserTopic + this.myUser.sessionId;
  }

  onClickGenerateQuestion(){
    this.gameDataService.generateQuestion(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        //this.currentQuestion = data
      }
    )
  }

  onClickStartQuiz(){
    this.loaderStartQuiz = true;
    console.log("User array: " + this.users)
    console.log("User array length: " + this.users.length)
    
    if (this.users.length <= 1) {
      this.enterMuseumIdSolo();
    }
    else {
      this.gameDataService.startSession(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          this.onClickGenerateQuestion();
          this.loaderStartQuiz = false;
          this.isGameStarted = true;
        }
      )
    }
  }

  /**
   * Function if host tries to start session by himself.
   */
  enterMuseumIdSolo(){
    console.log("Solo session with museum ID: " + this.myUser.museumId)
    this.loaderStartQuiz = true;
    this.gameDataService.soloSession(sessionStorage.getItem('userId') as string, this.myUser.museumId).subscribe(
      data => {
        this.isGameStarted = false;
        sessionStorage.setItem('isSolo', "true")
        this.loaderStartQuiz = false;
        this.router.navigate(['solo'])
      }
    )
  }

  onClickEndSession(){
    this.pausePauseTimer();
    this.pauseTimer();
    this.gameDataService.switchToResults(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.isGameStarted = false
        this.router.navigate(['gameover'])
      }
    )
  }

  onClickOption(i:number){
    //this.pauseTimer()
    // Below line of code selects clicked button from its id and changes its style
    document.getElementById(i.toString())!.style.backgroundColor = "#ffb74d";
    document.getElementById(i.toString())!.style.color = "white";
    this.clicked = true;
    if(this.currentQuestion.correctChoiceIndex == i){
      this.userDataService.addPointsToUser(this.myUser.userId, this.timeLeft*10).subscribe(
        data=>{
          console.log("CORRECT, POINTS: ", this.timeLeft*10 );
          this.isAnswerCorrect = true;
        }
      )
    }
    else{
      console.log("FALSE");
      this.isAnswerCorrect = false
    }
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
        this.timerRatioString = ((this.timeLeft / 30) * 100).toString() + "%";
      } else {
        this.resetPauseTimer();
        this.setUsers();
        this.isGamePaused = true;
        this.startPauseTimer();
        this.pauseTimer();
      }
    },1000)
  }

  resetTimer(){
    this.timeLeft = 30
  }

  pauseTimer() {
    clearInterval(this.interval);
  }

  startPauseTimer() {
    this.pauseInterval = setInterval(() => {
      if(this.pauseTimeLeft > 0) {
        this.pauseTimeLeft--;
      } else {
        this.gameDataService.checkPause(this.myUser.sessionId as string).subscribe(
          data => {
            if(data == true){
              this.onClickGenerateQuestion();
              //this.isGamePaused = false;
              this.pausePauseTimer();
            }
            else{
              this.pauseTimeLeft = 3;
            }
          }
        )
      }
    },1000)
  }

  pausePauseTimer() {
    clearInterval(this.pauseInterval);
  }

  resetPauseTimer() {
    this.pauseTimeLeft = 10
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
    let question: Question = JSON.parse(message.body);
    if(question.questionStatement === "endsession"){
      question.questionStatement = "Your session has ended.";
      this.onClickEndSession();
    }
    this.currentQuestion = question;
    this.isAnswerCorrect = false;
    this.resetTimer();
    this.startTimer();
    this.isGamePaused = false;
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

  //End of WebSocket ---------------------------------------------------------------------

}
