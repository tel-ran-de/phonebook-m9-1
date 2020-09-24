import {Component, Input, OnInit} from '@angular/core';
import {Email} from "../../../../model/email";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  @Input()
  emailToDisplay: Email;
  constructor() { }

  ngOnInit(): void {
  }

}
