import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameDataService } from '../service/game-data.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  hostSessionClicked: boolean = false
  joinSessionClicked: boolean = false
  soloSessionClicked: boolean = false
  sessionId: string = ''
  museumId: string = ''
  loader = false;

  constructor(private router: Router,
    private gameDataService: GameDataService) { }

  ngOnInit(): void {
  }

  enterMuseumIdHost(){
    console.log("Host session as: " + sessionStorage.getItem('userId') as string)
    console.log("Host session with museum ID: " + this.museumId)
    this.loader = true;
    this.gameDataService.hostSession(sessionStorage.getItem('userId') as string, this.museumId).subscribe(
      data => {
        console.log("Session ID: " + data.sessionId);
        sessionStorage.setItem('isHost', "true")
        this.loader = false;
        this.router.navigate(['host'])
      }
    )
  }
  enterSessionId(){
    console.log("Join session with ID: " + this.sessionId)
    this.loader = true;
    this.gameDataService.joinSession(this.sessionId, sessionStorage.getItem('userId') as string).subscribe(
      data => {
        sessionStorage.setItem('isParticipant', "true")
        this.loader = false;
        this.router.navigate(['participant'])
      }
    )
    
  }
  enterMuseumIdSolo(){
    console.log("Solo session with museum ID: " + this.museumId)
    this.loader = true;
    this.gameDataService.soloSession(sessionStorage.getItem('userId') as string, this.museumId).subscribe(
      data => {
        sessionStorage.setItem('isSolo', "true")
        this.loader = false;
        this.router.navigate(['solo'])
      }
    )
  }

  onClickHostSession(){
    this.hostSessionClicked = true
    this.joinSessionClicked = false
    this.soloSessionClicked = false
  }
  onClickJoinSession(){
    this.hostSessionClicked = false
    this.joinSessionClicked = true
    this.soloSessionClicked = false
  }
  onClickSoloSession(){
    this.hostSessionClicked = false
    this.joinSessionClicked = false
    this.soloSessionClicked = true
  }
}
