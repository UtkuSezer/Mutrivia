import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  errorHeader = 'Something Wrong Happened';
  errorMessage = 'Make sure you entered a valid URL';

  constructor() { }

  ngOnInit(): void {
  }

}
