import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentConversationComponent } from './appointment-conversation.component';

describe('AppointmentConversationComponent', () => {
  let component: AppointmentConversationComponent;
  let fixture: ComponentFixture<AppointmentConversationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentConversationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentConversationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
