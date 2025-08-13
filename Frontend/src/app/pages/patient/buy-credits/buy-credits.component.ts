import { Component, OnInit } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { CreditService } from '../../../service/credit.service';
import { UiService } from '../../../service/ui.service';
import { finalize } from 'rxjs';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-buy-credits',
  imports: [InputTextModule, CardModule, StepperModule, ButtonModule, FormsModule, InputNumberModule, MessageModule, NgIf],
  templateUrl: './buy-credits.component.html',
  styleUrl: './buy-credits.component.css'
})
export class BuyCreditsComponent implements OnInit {

  public credits: number = 0;
  public newCredits: number = 5;
  public amountCharged: number = Math.round((5 * 0.1) + 5);
  constructor(private creditService: CreditService, private uiService: UiService) { }

  ngOnInit(): void {
    setTimeout(() => this.getCredits(), 0);
  }

  getAmountCharged(): number {
    return Math.round((this.newCredits * 0.1) + this.newCredits);
  }

  private getCredits() {
    this.uiService.showSpinner();
    this.creditService
      .getMyCredits()
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const data = response.responseBody;
          this.credits = data.credits;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError("Failed to fetch Credits");
        },
      });
  }
}
