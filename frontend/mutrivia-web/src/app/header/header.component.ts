import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService } from '../service/user-data.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router,
    private userDataService: UserDataService,) { }

  ngOnInit(): void {
  }

  goHome(){
    //TODO: Request
    this.userDataService.deleteUser(sessionStorage.getItem('userId') as string).subscribe(
      data => {
        console.log("User with ID: " + sessionStorage.getItem("userId") + " is deleted.");
        sessionStorage.removeItem("userId");
      }
    )
    this.router.navigate(["register"]);
  }

}
