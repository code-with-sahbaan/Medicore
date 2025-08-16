import { Component, HostListener } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { Router, RouterModule } from '@angular/router';
import { NgStyle } from '@angular/common';
import { getCurrentUserData } from '../../../core/auth.utils';

@Component({
  selector: 'app-patient-side-drawer',
  imports: [
    DrawerModule,
    ButtonModule,
    Ripple,
    AvatarModule,
    RouterModule,
    NgStyle,
  ],
  standalone: true,
  templateUrl: './patient-side-drawer.component.html',
  styleUrl: './patient-side-drawer.component.css',
})
export class PatientSideDrawerComponent {
  visible = false;
  isDesktop = true;
  currentUrl : string = 'patient/home';
  fullName: string = getCurrentUserData()?.fullName;
  menuItems = [
    { icon: 'pi pi-home', label: 'Dashboard', route: '/patient/home' },
    {
      icon: 'pi pi-calendar',
      label: 'Book Appointment',
      route: '/patient/bookAppointment',
    },
    {
      icon: 'pi pi-book',
      label: 'My Appointments',
      route: '/patient/appointments',
    },
    {
      icon: 'pi pi-microchip-ai',
      label: 'AI Symptom Checker',
      route: '/patient/aiChecker',
    },
    {
      icon: 'pi pi-dollar',
      label: 'Manage Credits',
      route: '/patient/manageCredits',
    }
  ];

  constructor(private router: Router) {
    this.currentUrl = this.router.url;
    // Subscribe to route changes
    this.router.events.subscribe(() => {
      this.currentUrl = this.router.url;
    });
  }

  ngOnInit() {
    this.checkScreenSize();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth >= 1025;
    this.visible = this.isDesktop;
  }

  toggleDrawer() {
    this.visible = !this.visible;
  }

  closeCallback() {
    if (!this.isDesktop) {
      this.visible = false;
    }
  }
}
