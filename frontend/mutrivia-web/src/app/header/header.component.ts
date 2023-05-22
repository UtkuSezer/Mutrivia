import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService } from '../service/user-data.service';
import { GameDataService } from '../service/game-data.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router,
    private userDataService: UserDataService,
    private gameDataService: GameDataService) { }

  ngOnInit(): void {
  }

  goHome(){
    if(sessionStorage.getItem('isHost') == "true"){
      this.gameDataService.switchToResults(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          this.gameDataService.endSession(sessionStorage.getItem('userId') as string).subscribe(
            data => {
              sessionStorage.removeItem("userId");
              sessionStorage.removeItem("isHost");
            }
          )
        }
      )
    }
    else if(sessionStorage.getItem('isParticipant') == "true"){
      this.gameDataService.leaveSession(sessionStorage.getItem('userId') as string).subscribe(
        data => {
          sessionStorage.removeItem("userId");
          sessionStorage.removeItem("isParticipant");
        }
      )
    }
    this.router.navigate(["register"]);
  }

}
