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
    this.router.navigate(["register"]);
  }

}
