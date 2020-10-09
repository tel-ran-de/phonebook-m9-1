import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-sort-icon',
  templateUrl: './sort-icon.component.html',
  styleUrls: ['./sort-icon.component.css']
})
export class SortIconComponent implements OnInit {
  @Input()
  show: boolean;
  @Input()
  iconUp: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

}
