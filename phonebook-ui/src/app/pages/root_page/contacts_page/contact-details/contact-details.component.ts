import {Component, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../../service/user.service";

@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css']
})
export class ContactDetailsComponent implements OnInit {
  private token: number;

  constructor(private fb: FormBuilder, private userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.token = +this.route.snapshot.paramMap.get('token');

  }

}
