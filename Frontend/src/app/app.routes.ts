import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { OtpVerifictionComponent } from './pages/otp-verifiction/otp-verifiction.component';

export const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    data: {
      title: 'Login',
    },
  },
  {
    path: 'register',
    component: SignupComponent,
    data: {
      title: 'Register',
    },
  },
  {
    path: 'verifyOtp',
    component: OtpVerifictionComponent,
    data: {
      title: 'Verify OTP',
    },
  },
];
