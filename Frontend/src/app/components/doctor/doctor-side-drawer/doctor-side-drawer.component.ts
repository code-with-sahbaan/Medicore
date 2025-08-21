import { Component, HostListener, OnInit } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { Router, RouterModule } from '@angular/router';
import { NgStyle } from '@angular/common';
import { getCurrentUserData } from '../../../core/auth.utils';

@Component({
  selector: 'app-doctor-side-drawer',
  imports: [
    DrawerModule,
    ButtonModule,
    Ripple,
    AvatarModule,
    RouterModule,
    NgStyle,
  ],
  templateUrl: './doctor-side-drawer.component.html',
  styleUrl: './doctor-side-drawer.component.css'
})
export class DoctorSideDrawerComponent implements OnInit {
  visible = false;
  isDesktop = true;
  currentUrl: string = 'patient/home';
  fullName: string = getCurrentUserData()?.fullName;
  menuItems = [
    { 
      icon: 'pi pi-home', 
      label: 'Dashboard', 
      route: '/doctor/home' },
    {
      icon: 'pi pi-book',
      label: 'My Appointments',
      route: '/doctor/appointments',
    },
    {
      icon: 'pi pi-dollar',
      label: 'Payout Credits',
      route: '/doctor/payout',
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
