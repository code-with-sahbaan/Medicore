import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { PasswordModule } from 'primeng/password';
import { SplitterModule } from 'primeng/splitter';
import { ApiService } from '../../service/api.service';
import { UiService } from '../../service/ui.service';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-forgot-password',
  imports: [ButtonModule,
    SplitterModule,
    InputTextModule,
    Message,
    FormsModule,
    PasswordModule,
    ReactiveFormsModule,
    NgIf,],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  forgotPasswordForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private uiService: UiService,
    private router: Router,
    private authService: AuthService
  ) {
    this.forgotPasswordForm = fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  get getFormControls() {
    return this.forgotPasswordForm?.controls;
  }

  onSubmit() {
      if (this.forgotPasswordForm?.invalid) {
        this.forgotPasswordForm.markAllAsTouched();
        return;
      }
      /**
       * Showing Loader
       */
      this.uiService.showSpinner();
      /**
       * Calling API
       */
      const data = this.forgotPasswordForm.value;
      this.api
        .forgotPassword(data)
        .pipe(
          finalize(() => {
            // Hiding Loader after API call completion
            this.uiService.hideSpinner();
          })
        )
        .subscribe({
          next: (response) => {
            // Showing success Toast
            this.authService.email = this.forgotPasswordForm.get('email')?.value;
            this.router.navigate(['resetPassword']);
          },
          error: (error) => {
            // Showing error toast
            this.uiService.showError(error.error.responseMessage);
          },
        });
    }

}
