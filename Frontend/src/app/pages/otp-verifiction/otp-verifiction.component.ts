import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputOtp } from 'primeng/inputotp';
import { ApiService } from '../../service/api.service';
import { Router } from '@angular/router';
import { UiService } from '../../service/ui.service';
import { NgIf } from '@angular/common';
import { Message } from 'primeng/message';
import { Button } from 'primeng/button';
import { AuthService } from '../../service/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-otp-verifiction',
  imports: [FormsModule, InputOtp, NgIf, Message, Button, ReactiveFormsModule],
  templateUrl: './otp-verifiction.component.html',
  styleUrl: './otp-verifiction.component.css',
})
export class OtpVerifictionComponent implements OnInit {
  otpForm: FormGroup;

  ngOnInit(): void {
    if (this.authService.email.length == 0) this.router.navigate(['register']);
  }

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private router: Router,
    private uiService: UiService,
    public authService: AuthService
  ) {
    this.otpForm = fb.group({
      otp: ['', [Validators.required]],
    });
  }

  get getFormControls() {
    return this.otpForm?.controls;
  }

  onSubmit() {
    if (this.otpForm?.invalid) {
      this.otpForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    let data = this.otpForm.value;
    data.email = this.authService.email;
    this.api
      .verifyOtp(data)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("OTP verified successfully");
          this.router.navigate(['/']);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
