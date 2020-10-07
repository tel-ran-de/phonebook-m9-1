import {Component, Input, OnInit} from '@angular/core';
import {Email} from "src/app/model/email";
import {EmailService} from "../../../../service/email.service";

@Component({
  selector: 'app-email-table',
  templateUrl: './email-table.component.html',
  styleUrls: ['./email-table.component.css']
})
export class EmailTableComponent implements OnInit {

  @Input()
  sortedEmailsToDisplay: Email[];
  reverseSort: boolean;

  constructor(private emailService: EmailService) {
  }

  ngOnInit(): void {
    this.sortedEmailsToDisplay
      .sort((emailNameA, emailNameB) => emailNameA.id > emailNameB.id ? -1 : 1);
  }

  sort() {
    this.reverseSort = !this.reverseSort;
    this.sortedEmailsToDisplay
      .sort((emailNameA, emailNameB) => emailNameA.email > emailNameB.email ? -1 : 1);
    if (this.reverseSort)
      this.sortedEmailsToDisplay.reverse();
  }
  onClickRemove(id: number) {
    this.emailService.removeEmail(id);
  }
}
