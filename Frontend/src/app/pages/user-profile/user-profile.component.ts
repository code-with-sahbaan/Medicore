import { Component, OnInit } from '@angular/core';
import { ApiService, UserProfile } from '../../service/api.service';
import { UiService } from '../../service/ui.service';
import { finalize } from 'rxjs';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DatePicker } from 'primeng/datepicker';
import { FileUpload } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { NgIf } from '@angular/common';
import { logout } from '../../core/auth.utils';

@Component({
  selector: 'app-user-profile',
  imports: [InputTextModule, ReactiveFormsModule, FormsModule, InputNumberModule, FloatLabelModule, DatePicker, ButtonModule, NgIf],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {

  userProfile: UserProfile = {
    fullName: '',
    email: '',
    workingHourStart: new Date(),
    workingHourEnd: new Date(),
    consultationRates: 0
  };

  constructor(public apiService: ApiService, private uiService: UiService) { }

  ngOnInit(): void {
    setTimeout(() => this.getUserProfile(), 0)
  }

  getUserProfile() {
    /**
         * Showing Loader
         */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    this.apiService
      .getMyProfile()
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const data = response.responseBody;
          this.userProfile = data;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  updateUserProfile() {
    /**
         * Showing Loader
         */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    this.apiService
      .updateProfile(this.userProfile)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  logout(){
    logout();
  }
}
