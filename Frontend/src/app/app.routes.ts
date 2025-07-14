import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { OtpVerifictionComponent } from './pages/otp-verifiction/otp-verifiction.component';
import { PatientSideDrawerComponent } from './components/patient/patient-side-drawer/patient-side-drawer.component';
import { PatientDashboardComponent } from './pages/patient/patient-dashboard/patient-dashboard.component';

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
  // Patients Dashboard Menu
  { path: 'patient', redirectTo: 'patient/home' },
  {
    path: 'patient',
    component: PatientSideDrawerComponent,
    children: [{ path: 'home', component: PatientDashboardComponent }],
  },
  // Default Fallback
  {
    path: '**',
    redirectTo: '',
  },
];
