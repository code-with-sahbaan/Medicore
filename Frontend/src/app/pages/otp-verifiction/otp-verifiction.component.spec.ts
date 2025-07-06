import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtpVerifictionComponent } from './otp-verifiction.component';

describe('OtpVerifictionComponent', () => {
  let component: OtpVerifictionComponent;
  let fixture: ComponentFixture<OtpVerifictionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OtpVerifictionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OtpVerifictionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
