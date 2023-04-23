import { Component } from '@angular/core';
import { AdminService } from '../service/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
  museumId:string = "";
  artifactText:string = "";

  constructor(private router: Router,
    private adminService: AdminService, ) {}

  sendText(){
    this.adminService.generateQuestion(this.museumId,this.artifactText).subscribe(
      data => {
        this.artifactText = "";
      }
    )
  }
}
