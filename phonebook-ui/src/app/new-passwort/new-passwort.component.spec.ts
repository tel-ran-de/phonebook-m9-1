import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPasswortComponent } from './new-passwort.component';

describe('NewPasswortComponent', () => {
  let component: NewPasswortComponent;
  let fixture: ComponentFixture<NewPasswortComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewPasswortComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewPasswortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
