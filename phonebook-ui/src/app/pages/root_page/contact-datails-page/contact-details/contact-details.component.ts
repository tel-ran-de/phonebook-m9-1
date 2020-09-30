import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css'],
})
export class ContactDetailsComponent implements OnInit, OnDestroy {

  contactId: number;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.contactId = +this.route.snapshot.paramMap.get('contactId');
  }

  ngOnDestroy(): void {
    this.contactId = null;
  }
}
