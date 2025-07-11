import { Component, HostListener, ViewChild } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { StyleClass } from 'primeng/styleclass';
import { Drawer } from 'primeng/drawer';
import { RouterModule } from '@angular/router';
import { NgIf, NgStyle } from '@angular/common';

@Component({
  selector: 'app-patient-side-drawer',
  imports: [DrawerModule, ButtonModule, Ripple, AvatarModule, RouterModule, NgStyle, NgIf],
  standalone: true,
  templateUrl: './patient-side-drawer.component.html',
  styleUrl: './patient-side-drawer.component.css',
})
export class PatientSideDrawerComponent {
  visible = false;
  isDesktop = true;

  ngOnInit() {
    this.checkScreenSize();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth >= 1024;
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
