import {Component, Input, OnInit} from '@angular/core';
import {Email} from "src/app/model/email";

@Component({
  selector: 'app-email-table',
  templateUrl: './email-table.component.html',
  styleUrls: ['./email-table.component.css']
})
export class EmailTableComponent implements OnInit {

  @Input()
  sortedEmailsToDisplay: Email[];
  reverseSort: boolean;

  constructor() {
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
}
