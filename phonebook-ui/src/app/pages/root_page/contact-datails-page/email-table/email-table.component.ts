import {Component, Input, OnInit} from '@angular/core';
import {Email} from "src/app/model/email";

@Component({
  selector: 'app-email-table',
  templateUrl: './email-table.component.html',
  styleUrls: ['./email-table.component.css']
})
export class EmailTableComponent implements OnInit {

  @Input()
  emailsToDisplay: Email[];
  @Input()
  isSearchTable: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

  generateId(elementId: string): string {
    return this.isSearchTable ? elementId + "-search" : elementId;
  }

}
