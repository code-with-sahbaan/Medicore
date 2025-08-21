import { Component, OnInit } from '@angular/core';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-payout',
  imports: [],
  templateUrl: './payout.component.html',
  styleUrl: './payout.component.css'
})
export class PayoutComponent implements OnInit {

  ngOnInit(): void {
    setTimeout(() => this.printBankToken(), 0)
  }

  async printBankToken() {
    const stripe = await loadStripe(environment.stripePublicKey) as Stripe;
    const bankAccountToken = await stripe.createToken('bank_account', {
      country: 'US',
      currency: 'usd',
      routing_number: '110000000',
      account_number: '000123456789',
      account_holder_name: 'Jenny Rosen',
      account_holder_type: 'individual',
    });
    console.log(bankAccountToken);
  }
}
