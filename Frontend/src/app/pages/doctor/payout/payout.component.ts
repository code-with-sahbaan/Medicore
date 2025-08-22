import { Component, OnInit } from '@angular/core';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import { environment } from '../../../../environments/environment';
import { CardModule } from 'primeng/card';
import { UiService } from '../../../service/ui.service';
import { CreditService, PayoutCredits } from '../../../service/credit.service';
import { finalize } from 'rxjs';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';
import { SelectModule } from 'primeng/select';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-payout',
  imports: [CardModule, InputTextModule, InputNumberModule, FloatLabelModule, FormsModule, ButtonModule, MessageModule, ReactiveFormsModule, NgIf, SelectModule],
  templateUrl: './payout.component.html',
  styleUrl: './payout.component.css'
})
export class PayoutComponent implements OnInit {

  withdrawForm: FormGroup;
  credits: number = 0;
  holderType: string[] = ['Individual', 'Company'];
  SUPPORTED_COUNTRIES: string[] = [
    "AU", "AT", "BE", "BR", "BG", "CA", "HR", "CY", "CZ", "DK",
    "EE", "FI", "FR", "DE", "GR", "HK", "IE", "IT", "JP", "LV",
    "LT", "LU", "MT", "MX", "NL", "NZ", "NO", "PL", "PT", "RO",
    "SG", "SK", "SI", "ES", "SE", "CH", "GB", "US"
  ];
  SUPPORTED_CURRENCIES: string[] = [
    "aud", "eur", "brl", "bgn", "cad", "czk", "dkk", "hkd", "jpy",
    "mxn", "nzd", "nok", "pln", "ron", "sgd", "sek", "chf", "gbp", "usd"
  ];

  constructor(private fb: FormBuilder, public uiService: UiService, public creditService: CreditService, private route: ActivatedRoute) {
    this.withdrawForm = fb.group({
      amount: [5, [Validators.required]],
      country: ['US', [Validators.required]],
      currency: ['usd', [Validators.required]],
      routing_number: ['', [Validators.required]],
      account_number: ['', [Validators.required]],
      account_holder_name: ['', [Validators.required]],
      account_holder_type: ['Individual', [Validators.required]],
    });
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.getCredits();
      const status = this.route.snapshot.queryParamMap.get('verificationStatus');
      const token = this.route.snapshot.queryParamMap.get('bankToken') ?? '';
      const currency = this.route.snapshot.queryParamMap.get('currency') ?? '';
      const credits = this.route.snapshot.queryParamMap.get('credits') ?? '';
      if (status?.length != 0 && token?.length != 0) {
        this.withdrawForm.get('amount')?.setValue(credits);
        this.withdrawForm.get('currency')?.setValue(currency);
        this.updateVerification(token);
      }
    }, 0);

  }

  get getFormControls() {
    return this.withdrawForm?.controls;
  }

  async sendPayout() {
    if (this.withdrawForm.invalid) {
      return;
    }
    const stripe = await loadStripe(environment.stripePublicKey) as Stripe;
    this.uiService.showSpinner();
    stripe.createToken('bank_account', this.withdrawForm.value).
      then((result) => {
        if (result.error) {
          this.uiService.showError("Incorrect or Missing Bank Details");
        } else {
          this.payoutCredits(result.token.id);
        }
      })
  }

  private payoutCredits(bankToken: string) {
    const payload: PayoutCredits = {
      bankToken: bankToken,
      credits: this.withdrawForm.get('amount')?.value,
      currency: this.withdrawForm.get('currency')?.value,
      country: this.withdrawForm.get('country')?.value,
      account_holder_type: this.withdrawForm.get('account_holder_type')?.value
    }
    this.creditService
      .payoutCredits(payload)
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
          this.credits = this.credits - Number(this.withdrawForm.get('amount')?.value);
        },
        error: (error) => {
          // Showing error toast
          const msg: string = error.error.responseMessage;
          if (msg.includes("https")) {
            window.location.href = msg;
          } else {
            this.uiService.showError("Failed to Withdraw Credits");
          }
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

  getAmountCharged(): number {
    const newCredits = Number(this.getFormControls['amount'].value);
    const platformCharges:number = 1 - (environment.platformCharges / 100);
    return newCredits * platformCharges;
  }

  private updateVerification(bankToken: string) {
    this.uiService.showSpinner();
    this.creditService
      .updateVerification()
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
          this.payoutCredits(bankToken);
        },
        error: (error) => {
          // Showing error toast\
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
