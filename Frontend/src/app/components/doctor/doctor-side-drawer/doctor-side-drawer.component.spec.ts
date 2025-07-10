import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorSideDrawerComponent } from './doctor-side-drawer.component';

describe('DoctorSideDrawerComponent', () => {
  let component: DoctorSideDrawerComponent;
  let fixture: ComponentFixture<DoctorSideDrawerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorSideDrawerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorSideDrawerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
