import {Component, Input, OnInit} from '@angular/core';
import {ContactService} from "../../../../service/contact.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Contact} from "../../../../model/contact";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  @Input()
  contactId: number;
  public form: FormGroup;

  constructor(private contactServes: ContactService, private fb: FormBuilder,) {
  }

  ngOnInit(): void {
    this.createForm();

    this.contactServes.getContactById(this.contactId).subscribe(value => {
      this.form.controls['firstName'].setValue(value.firstName);
      this.form.controls['lastName'].setValue(value.lastName);
      this.form.controls['about'].setValue(value.description);
    })
  }

  createForm() {
    this.form = this.fb.group({
      firstName: [],
      lastName: [],
      about: [],
    });
  }

  onSubmit() {
  }
}
