import { Component, HostListener } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { GameDataService } from './service/game-data.service'
import { UserDataService } from './service/user-data.service'
import { getLocaleTimeFormat } from '@angular/common';

export let browserRefresh = false;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mutrivia-web';
  fullLoader = false

  subscription: Subscription | undefined;

  @HostListener('window:unload', [ '$event' ])
  unloadHandler(event: any) {
    // ...

    
  }

  @HostListener('window:beforeunload', [ '$event' ])
  beforeUnloadHandler(event: any) {
    // ...
    this.fullLoader = true
    if(sessionStorage.getItem('userId') as string == null){
      this.fullLoader = false
      return true;
    }
    else{
      console.log("User To Delete: " + sessionStorage.getItem('userId') as string)  
      if(sessionStorage.getItem('isHost') == "true"){
        this.gameDataService.switchToResults(sessionStorage.getItem('userId') as string).subscribe(
          data => {
            this.gameDataService.endSession(sessionStorage.getItem('userId') as string).subscribe(
              data => {
                this.userDataService.deleteUser(sessionStorage.getItem('userId') as string).subscribe(
                  data => {
                     sessionStorage.removeItem("userId");
                     this.fullLoader = false
                     this.router.navigate(['register']);
                  }
                )
              }
            )
          }
        )
      }
      else if(sessionStorage.getItem('isParticipant') == "true"){
        this.gameDataService.leaveSession(sessionStorage.getItem('userId') as string).subscribe(
          data => {
            this.userDataService.deleteUser(sessionStorage.getItem('userId') as string).subscribe(
              data => {
                 sessionStorage.removeItem("userId");
                 this.fullLoader = false
                 this.router.navigate(['register']);
              }
            )
          }
        )
      }
      else{
        this.userDataService.deleteUser(sessionStorage.getItem('userId') as string).subscribe(
          data => {
             sessionStorage.removeItem("userId");
             this.fullLoader = false
             this.router.navigate(['register']);
          }
        )
      }
      return false;
    }

    
  }

  constructor(private router: Router, private userDataService: UserDataService, private gameDataService: GameDataService) {
    this.subscription = router.events.subscribe((event) => {
        if (event instanceof NavigationStart) {
          browserRefresh = !router.navigated;
        }
    });
  }
}
