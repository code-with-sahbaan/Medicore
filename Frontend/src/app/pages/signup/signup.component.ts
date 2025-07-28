import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Checkbox } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { PasswordModule } from 'primeng/password';
import { Select } from 'primeng/select';
import { SplitterModule } from 'primeng/splitter';
import { ApiService } from '../../service/api.service';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { UiService } from '../../service/ui.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-signup',
  imports: [
    ButtonModule,
    SplitterModule,
    InputTextModule,
    Message,
    FormsModule,
    PasswordModule,
    ReactiveFormsModule,
    NgIf,
    Select,
    Checkbox
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  singupForm: FormGroup;
  roles: string[] = ['Doctor', 'Patient'];

  constructor(private fb: FormBuilder, private api: ApiService, private router: Router, private uiService: UiService, private authService : AuthService) {
    this.singupForm = fb.group({
      fullName: ['', [Validators.required]],
      role: ['Owner', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      TC:['', Validators.required]
    });
  }

  get getFormControls() {
    return this.singupForm?.controls;
  }

  onSubmit() {
    if (this.singupForm?.invalid) {
      this.singupForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    const data = this.singupForm.value;
    this.api.signup(data).pipe(
      finalize(()=>{
        // Hiding Loader after API call completion
        this.uiService.hideSpinner();
      })
    ).subscribe({
      next: response =>{
        // Showing success Toast
        this.uiService.showSuccess(response.responseMessage);
        this.authService.email = this.singupForm.get('email')?.value;
        this.router.navigate(['verifyOtp']);
      },
      error : error =>{
        // Showing error toast
        this.uiService.showError(error.error.responseMessage);
      }
    });
  }
}
