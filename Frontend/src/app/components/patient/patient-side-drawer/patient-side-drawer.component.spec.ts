import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientSideDrawerComponent } from './patient-side-drawer.component';

describe('PatientSideDrawerComponent', () => {
  let component: PatientSideDrawerComponent;
  let fixture: ComponentFixture<PatientSideDrawerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatientSideDrawerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PatientSideDrawerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
