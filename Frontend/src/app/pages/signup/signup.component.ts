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
  roles: string[] = ['Owner', 'Doctor', 'Patient'];

  constructor(private fb: FormBuilder, private api: ApiService, private router: Router) {
    this.singupForm = fb.group({
      fullName: ['', [Validators.required]],
      role: ['', Validators.required],
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
    const data = this.singupForm.value;
    this.api.signup(data).subscribe({
      next: response =>{
        this.router.navigate(['/']);
      },
      error : error =>{
        console.log(error);
      }
    });
  }
}
