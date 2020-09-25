import {Component, Input, OnInit} from '@angular/core';
import {EmailService} from "../../../../service/email.service";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  @Input()
  contactId: number;
  constructor(public emailService: EmailService) { }

  ngOnInit(): void {
    this.emailService.getAllEmailsByContactId(this.contactId);
  }

}
