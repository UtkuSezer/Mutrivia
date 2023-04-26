import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { GameDataService } from '../service/game-data.service';
import { UserDataService } from '../service/user-data.service';
import { browserRefresh } from '../app.component';

@Component({
  selector: 'app-gameover',
  templateUrl: './gameover.component.html',
  styleUrls: ['./gameover.component.css']
})
export class GameoverComponent implements OnInit {

  users: User[] = []
  myUser!: User

  constructor(private gameDataService: GameDataService,
    private userDataService: UserDataService,
    private router: Router) { }

  ngOnInit(): void {
    if(browserRefresh){
      this.onClickEndSession();
    }
    else{
      this.userDataService.getUsersInSession(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          this.users = data;
          this.users.sort((firstUser, secondUser) => secondUser.score - firstUser.score);
        }
      )
      this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          this.myUser = data
        }
      )
    }
  }

  onClickEndSession(){
    if(sessionStorage.getItem('isHost')){
      this.gameDataService.endSession(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          sessionStorage.removeItem("isHost");
          this.router.navigate(['menu'])
        }
      )
    }
    if(sessionStorage.getItem('isParticipant')){
      this.gameDataService.leaveSession(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          sessionStorage.removeItem("isParticipant");
          this.router.navigate(['menu'])
        }
      )
    }
  }

}
