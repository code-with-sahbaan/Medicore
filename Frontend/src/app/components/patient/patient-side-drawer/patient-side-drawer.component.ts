import { Component, ViewChild } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { StyleClass } from 'primeng/styleclass';
import { Drawer } from 'primeng/drawer';

@Component({
  selector: 'app-patient-side-drawer',
  imports: [DrawerModule, ButtonModule, Ripple, AvatarModule, StyleClass],
  standalone:true,
  templateUrl: './patient-side-drawer.component.html',
  styleUrl: './patient-side-drawer.component.css',
})
export class PatientSideDrawerComponent {
  @ViewChild('drawerRef') drawerRef!: Drawer;

  closeCallback(e: any): void {
    this.drawerRef.close(e);
  }

  visible: boolean = true;
}
