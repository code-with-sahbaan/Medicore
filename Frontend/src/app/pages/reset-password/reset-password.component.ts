import { NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { InputOtp } from 'primeng/inputotp';
import { Message } from 'primeng/message';
import { ApiService } from '../../service/api.service';
import { Router } from '@angular/router';
import { UiService } from '../../service/ui.service';
import { AuthService } from '../../service/auth.service';
import { finalize } from 'rxjs';
import { PasswordModule } from 'primeng/password';

@Component({
  selector: 'app-reset-password',
  imports: [FormsModule, InputOtp, NgIf, Message, Button, ReactiveFormsModule, PasswordModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {
  
  resetForm: FormGroup;

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
    this.resetForm = fb.group({
      otp: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  get getFormControls() {
    return this.resetForm?.controls;
  }

  onSubmit() {
    if (this.resetForm?.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    let data = this.resetForm.value;
    data.email = this.authService.email;
    data.verificationType = "forgotPassword";
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
          this.uiService.showSuccess("Password Updated successfully");
          this.router.navigate(['/']);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
