import { Component, OnInit } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { BuyCredits, CreditService } from '../../../service/credit.service';
import { UiService } from '../../../service/ui.service';
import { finalize } from 'rxjs';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';
import { environment } from '../../../../environments/environment';
import {
  loadStripe,
  StripeElements,
  StripePaymentElement,
  type Stripe,           // <-- 'type' here is important for TS
  type StripeCardElement
} from '@stripe/stripe-js';
import { Router } from '@angular/router';


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
  // Stripe vars
  stripe!: Stripe;
  elements!: StripeElements;
  paymentElement!: StripePaymentElement;
  quantity = 1;
  message = '';
  loading = false;
  clientSecrte: any;

  constructor(private creditService: CreditService, private uiService: UiService, private router: Router) { }

  ngOnInit(): void {
    setTimeout(() => this.getCredits(), 0);
  }

  getAmountCharged(): number {
    return Math.round((this.newCredits * 0.1) + this.newCredits);
  }

  onStepChange(e: number | undefined) {
    if (e == 2) {
      setTimeout(() => {
        this.setupStripe();
      }, 0);
    }
  }

  async setupStripe() {
    this.stripe = await loadStripe(environment.stripePublicKey) as Stripe;
    this.createPaymentIntent();
  }

  private createPaymentIntent() {
    this.uiService.showSpinner();
    const buyCredits: BuyCredits = {
      credits: this.newCredits
    }
    this.creditService
      .createPaymentIntent(buyCredits)
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
          this.clientSecrte = data.clientSecret;
          this.elements = this.stripe.elements({ clientSecret: data.clientSecret });
          this.paymentElement = this.elements.create('payment');
          this.paymentElement.mount('#payment-element');
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError("Failed to initiate payment");
        },
      });
  }

  async payNow() {
    this.uiService.showSpinner();
    this.elements.submit();
    await this.stripe.confirmPayment({ elements: this.elements, clientSecret: this.clientSecrte, redirect: 'if_required' })
      .then((response) => {
        this.uiService.showSuccess("Payment Successful");
        this.updateCredits();
      }).catch((error) => {
        console.log(error);
        this.uiService.showError("Failed to Charge Payment! Try Again");
      }).finally(() => {
        this.uiService.hideSpinner();
      });

  }

  private updateCredits() {
    this.uiService.showSpinner();
    const buyCredits: BuyCredits = {
      credits: this.newCredits
    }
    this.creditService
      .updateCredits(buyCredits)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.router.navigate(['patient'])
        },
        error: (error) => {
          // Showing error toast\
          this.uiService.showError("Failed to Update Credits");
        },
      });
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
          // Showing error toast\
          this.uiService.showError("Failed to fetch Credits");
        },
      });
  }
}
