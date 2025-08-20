import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { OtpVerifictionComponent } from './pages/otp-verifiction/otp-verifiction.component';
import { PatientSideDrawerComponent } from './components/patient/patient-side-drawer/patient-side-drawer.component';
import { PatientDashboardComponent } from './pages/patient/patient-dashboard/patient-dashboard.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { BookAppointmentComponent } from './pages/patient/book-appointment/book-appointment.component';
import { MyAppointmentsComponent } from './pages/patient/my-appointments/my-appointments.component';
import { AppointmentConversationComponent } from './pages/appointment-conversation/appointment-conversation.component';
import { BuyCreditsComponent } from './pages/patient/buy-credits/buy-credits.component';
import { AiSymptomCheckerComponent } from './pages/patient/ai-symptom-checker/ai-symptom-checker.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { DoctorSideDrawerComponent } from './components/doctor/doctor-side-drawer/doctor-side-drawer.component';
import { DoctorDashboardComponent } from './pages/doctor/doctor-dashboard/doctor-dashboard.component';

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
  {
    path: 'forgotPassword',
    component: ForgotPasswordComponent,
    data: {
      title: 'Forgot Password',
    },
  },
  {
    path: 'resetPassword',
    component: ResetPasswordComponent,
    data: {
      title: 'Reset Password',
    },
  },
  {
    path: 'appointmentConversation',
    component: AppointmentConversationComponent,
    data: {
      title: 'Appointment Conversation',
    },
  },
  // Patients Dashboard Menu
  { path: 'patient', redirectTo: 'patient/home' },
  {
    path: 'patient',
    component: PatientSideDrawerComponent,
    children: [
      {
        path: 'home', component: PatientDashboardComponent, data: {
          title: 'Home',
        },
      },
      {
        path: 'bookAppointment',
        component: BookAppointmentComponent,
        data: {
          title: 'Book Your Appointment',
        },
      },
      {
        path: 'appointments',
        component: MyAppointmentsComponent,
        data: {
          title: 'My Appointments',
        },
      },
      {
        path: 'manageCredits',
        component: BuyCreditsComponent,
        data: {
          title: 'Buy Credits',
        },
      },
      {
        path: 'aiChecker',
        component: AiSymptomCheckerComponent,
        data: {
          title: 'AI Checker',
        },
      },
      {
        path: 'userProfile',
        component: UserProfileComponent,
        data: {
          title: 'User Profile',
        },
      }
    ],
  },
  // Doctor Dashboard Menu
  { path: 'doctor', redirectTo: 'doctor/home' },
  {
    path: 'doctor',
    component: DoctorSideDrawerComponent,
    children: [
      {
        path: 'home', component: DoctorDashboardComponent, data: {
          title: 'Home',
        },
      },
      {
        path: 'appointments',
        component: MyAppointmentsComponent,
        data: {
          title: 'My Appointments',
        },
      },
      {
        path: 'userProfile',
        component: UserProfileComponent,
        data: {
          title: 'User Profile',
        },
      }
    ],
  },
  // Default Fallback
  {
    path: '**',
    redirectTo: '',
  },
];
