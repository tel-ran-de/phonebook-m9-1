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
  sortBy: string;

  constructor(private emailService: EmailService) {
  }

  ngOnInit(): void {
  }

  sort(sortBy: string) {
    if (this.sortBy !== sortBy)
      this.reverseSort = false;

    this.sortBy = sortBy;
    this.reverseSort = !this.reverseSort;

    this.sortedEmailsToDisplay.sort((a, b) => a[sortBy] > b[sortBy] ? -1 : 1)

    if (this.reverseSort)
      this.sortedEmailsToDisplay.reverse();
  }

  onClickRemove(emailId: number) {
    this.emailService.removeEmail(emailId);
  }
}
