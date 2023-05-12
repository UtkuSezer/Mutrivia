import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService } from '../service/user-data.service';
import { User } from '../models/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  username: string = '';
  loader = false;

  constructor(private router: Router,
    private userDataService: UserDataService, ) { }

  ngOnInit(): void {
    if (sessionStorage.getItem('userId')){
      this.userDataService.deleteUser(sessionStorage.getItem('userId')!).subscribe(
      data => {
        sessionStorage.removeItem('userId')
      }
    )}
    if(sessionStorage.getItem('isAdmin')){
      sessionStorage.removeItem('isAdmin')
    }   
  }

  registerUser(){
    if(this.username == ''){
    }
    else{
      console.log("User registered: " + this.username)
      this.loader = true;
      this.userDataService.addUser(this.username).subscribe(
        data => {
          sessionStorage.setItem('userId', data.userId)
          console.log("User ID set: " + data.userId)
          this.loader = false;
          this.router.navigate(['menu'])
        }
      )
    }
  }

  

}
