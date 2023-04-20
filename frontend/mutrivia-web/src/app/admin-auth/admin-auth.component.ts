import { Component } from '@angular/core';
import { AdminService } from '../service/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-auth',
  templateUrl: './admin-auth.component.html',
  styleUrls: ['./admin-auth.component.css']
})
export class AdminAuthComponent {

  id:string = "";
  password:string = "";

  constructor(private router: Router,
    private adminService: AdminService, ) {}

  authenticate(){
    this.adminService.authenticate(this.id,this.password).subscribe(
      data => {
        if(data == true){
          sessionStorage.setItem('isAdmin','true')
          this.router.navigate(['admin'])
        }
      }
    )
  }

}
