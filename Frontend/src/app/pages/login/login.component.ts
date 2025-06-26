import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SplitterModule } from 'primeng/splitter';
import { InputTextModule } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { NgIf } from '@angular/common';

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
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  standalone:true
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder) {
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
      console.log(this.loginForm?.invalid);
      this.loginForm.markAllAsTouched();
      return;
    }
  }
}
