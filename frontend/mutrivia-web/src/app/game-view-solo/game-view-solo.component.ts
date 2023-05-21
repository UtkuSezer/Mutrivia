import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Question } from '../models/question';
import { User } from '../models/user';
import { GameDataService } from '../service/game-data.service';
import { UserDataService } from '../service/user-data.service';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { browserRefresh } from '../app.component';

@Component({
  selector: 'app-game-view-solo',
  templateUrl: './game-view-solo.component.html',
  styleUrls: ['./game-view-solo.component.css']
})
export class GameViewSoloComponent implements OnInit {

  myUser!: User
  currentQuestion!: Question

  stompClient: any;
  webSocketEndPoint: string = 'http://www.mutrivia.com/api/ws';
  questionTopic: string = "/topic/question/";
  isAnswerCorrect: boolean = false

  timeLeft: number = 30;
  interval !: any;
  pauseInterval !: any;
  pauseTimeLeft: number = 5;
  isGamePaused: boolean = false
  isGameStarted: boolean = false
  timerRatioString: string = "100%";
  clicked = false;
  loaderEndQuiz = false;

  constructor(private userDataService: UserDataService,
    private gameDataService: GameDataService,
    private router: Router) { }

  ngOnInit(): void {
    if(browserRefresh){
      this.onClickLeaveGame();
    }
    this.pauseTimer();
    this.pausePauseTimer();
    this.resetTimer();
    this.resetPauseTimer();
    this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        this.myUser = data
        this.onClickGenerateQuestion();
      }
    )

  }

  onClickGenerateQuestion(){
    this.clicked = false;
    this.gameDataService.generateQuestion(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        if(data.questionStatement == "endsession"){
          data.questionStatement = "Your session has ended.";
          console.log("Finish Quiz")
          this.onClickLeaveGame();
        }
        else {
          this.currentQuestion = data
          console.log("Question Set")
          this.isAnswerCorrect = false;
          this.resetTimer();
          this.startTimer();
          this.isGamePaused = false;
        }
      }
    )
  }

  onClickOption(i:number){
    //this.pauseTimer()
    document.getElementById(i.toString())!.style.backgroundColor = "#ffb74d";
    document.getElementById(i.toString())!.style.color = "white";
    this.clicked = true;
    if(this.currentQuestion.correctChoiceIndex == i){
      this.userDataService.addPointsToUser(this.myUser.userId, this.timeLeft*10).subscribe(
        data=>{
          console.log("CORRECT, POINTS: ", this.timeLeft*10 );
          this.isAnswerCorrect = true;
          this.resetPauseTimer();
          this.isGamePaused = true;
          this.startPauseTimer();
          this.pauseTimer();
          this.myUser = data;
        }
      )
    }
    else{
      console.log("FALSE");
      this.isAnswerCorrect = false
      this.resetPauseTimer();
      this.isGamePaused = true;
      this.startPauseTimer();
      this.pauseTimer();
    }
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
        this.timerRatioString = ((this.timeLeft / 30) * 100).toString() + "%";
      } else {
        this.resetPauseTimer();
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
        this.onClickGenerateQuestion();
        this.pausePauseTimer();
        //this.isGamePaused = false;
      }
    },1000)
  }

  pausePauseTimer() {
    clearInterval(this.pauseInterval);
  }

  resetPauseTimer() {
    this.pauseTimeLeft = 5
  }

  onClickLeaveGame(){
    this.loaderEndQuiz = true;
    this.pausePauseTimer();
    this.pauseTimer();
    this.gameDataService.checkLeaderboard(this.myUser.userId as string).subscribe(
      data => {
        sessionStorage.removeItem("isSolo");
        this.router.navigate(['leaderboard']);
      }
    )
  }
    //End of WebSocket ---------------------------------------------------------------------

}
