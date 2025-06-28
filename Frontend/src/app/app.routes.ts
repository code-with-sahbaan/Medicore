import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';

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
];
