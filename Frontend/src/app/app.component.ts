import { Component } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';
import { Title } from '@angular/platform-browser';
import { filter, map, mergeMap } from 'rxjs';
import { ProgressSpinner } from 'primeng/progressspinner';
import { Toast } from 'primeng/toast';
import { UiService } from './service/ui.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ProgressSpinner, Toast, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  constructor(
    private titleService: Title,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public uiService: UiService,
  ) {}

  /**
   * Setting the title dynamically of each screen.
   */
  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => {
          let route = this.activatedRoute;
          while (route.firstChild) route = route.firstChild;
          return route;
        }),
        filter((route) => route.outlet === 'primary'),
        mergeMap((route) => route.data)
      )
      .subscribe((data: any) => {
        if (data['title']) {
          this.titleService.setTitle(`Medicore - ${data['title']}`);
        }
      });
  }
}
