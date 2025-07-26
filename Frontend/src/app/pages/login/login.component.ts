import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SplitterModule } from 'primeng/splitter';
import { InputTextModule } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { NgIf } from '@angular/common';
import { UiService } from '../../service/ui.service';
import { ApiService } from '../../service/api.service';
import { finalize } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [
  ButtonModule,
    SplitterModule,
    InputTextModule,
    Message,
    FormsModule,
    PasswordModule,
    ReactiveFormsModule,
    NgIf,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  standalone: true,
})
export class LoginComponent {

  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private uiService: UiService,
    private router: Router,
    private authService: AuthService 
  ) {
    this.loginForm = fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  get getFormControls() {
    return this.loginForm?.controls;
  }

  onSubmit() {
    if (this.loginForm?.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    const data = this.loginForm.value;
    this.api
      .login(data)
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
          const user = response.responseBody;
          if(user.isActive == false){
            this.authService.email = user.email;
            this.router.navigate(['verifyOtp']);
          }else{
            localStorage.setItem("USER", JSON.stringify(user));
            if(user.role == "PATIENT"){
              this.router.navigate(['patient']);
            }else{
              this.router.navigate(['doctor']);
            }
          }
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
