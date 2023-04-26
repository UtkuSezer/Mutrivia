import { Component } from '@angular/core';
import { UserDataService } from '../service/user-data.service';
import { Router } from '@angular/router';
import { browserRefresh } from '../app.component';
import { User } from '../models/user';
import { GameDataService } from '../service/game-data.service';
import { LeaderboardRecord } from '../models/leaderboard-record';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent {
  leaderboardRecords: LeaderboardRecord[] = []
  myUser!: User

  constructor(private gameDataService: GameDataService,
    private userDataService: UserDataService,
    private router: Router) { }

  ngOnInit(): void {
    if(browserRefresh){
      this.onClickEndSession();
    }
    else{
      this.userDataService.getUser(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          this.myUser = data
          this.gameDataService.getLeaderboard(this.myUser.userId as string).subscribe(
            data => {
              this.leaderboardRecords = data;
              this.leaderboardRecords.sort((firstRecord, secondRecord) => secondRecord.score - firstRecord.score);
            }
          )
        }
      )
    }
  }

  onClickEndSession(){
    this.gameDataService.endSession(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        sessionStorage.removeItem("isSolo");
        this.router.navigate(['menu'])
      }
    )
  }
}