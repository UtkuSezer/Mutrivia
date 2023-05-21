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
  idExists = true;
  sessionExists = true;

  constructor(private router: Router,
    private gameDataService: GameDataService) { }

  ngOnInit(): void {
  }

  enterMuseumIdHost(){
    if (this.museumId == '') return;
    console.log("Host session as: " + sessionStorage.getItem('userId') as string)
    console.log("Host session with museum ID: " + this.museumId)
    this.loader = true;
    this.gameDataService.hostSession(sessionStorage.getItem('userId') as string, this.museumId).subscribe(
      data => {
        if (data == null){
          this.idExists = false;
          this.loader = false;
          return;
        }
        console.log("Session ID: " + data.sessionId);
        sessionStorage.setItem('isHost', "true")
        this.loader = false;
        this.router.navigate(['host'])
      }
    )
  }
  enterSessionId(){
    if (this.sessionId == '') return;
    console.log("Join session with ID: " + this.sessionId)
    this.loader = true;
    this.gameDataService.joinSession(this.sessionId, sessionStorage.getItem('userId') as string).subscribe(
      data => {
        if (data == null){
          this.sessionExists = false;
          this.loader = false;
          return;
        }
        sessionStorage.setItem('isParticipant', "true")
        this.loader = false;
        this.router.navigate(['participant'])
      }
    )
    
  }
  enterMuseumIdSolo(){
    if (this.museumId == '') return;
    console.log("Solo session with museum ID: " + this.museumId)
    this.loader = true;
    this.gameDataService.soloSession(sessionStorage.getItem('userId') as string, this.museumId).subscribe(
      data => {
        if (data == null){
          this.idExists = false;
          this.loader = false;
          return;
        }
        sessionStorage.setItem('isSolo', "true")
        this.loader = false;
        this.router.navigate(['solo'])
      }
    )
  }

  onClickHostSession(){
    this.idExists = true;
    this.hostSessionClicked = true
    this.joinSessionClicked = false
    this.soloSessionClicked = false
  }
  onClickJoinSession(){
    this.sessionExists = true;
    this.hostSessionClicked = false
    this.joinSessionClicked = true
    this.soloSessionClicked = false
  }
  onClickSoloSession(){
    this.idExists = true;
    this.hostSessionClicked = false
    this.joinSessionClicked = false
    this.soloSessionClicked = true
  }
}
