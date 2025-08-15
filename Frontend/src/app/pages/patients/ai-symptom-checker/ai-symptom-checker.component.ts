import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputTextModule } from 'primeng/inputtext';
import { SkeletonModule } from 'primeng/skeleton';
import { AiService, AiUserMessage } from '../../../service/patient/ai.service';
import { finalize } from 'rxjs';
import { UiService } from '../../../service/ui.service';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'app-ai-symptom-checker',
  imports: [InputTextModule, FormsModule, ReactiveFormsModule, InputGroupModule, ButtonModule, SkeletonModule, NgIf, MarkdownModule],
  templateUrl: './ai-symptom-checker.component.html',
  styleUrl: './ai-symptom-checker.component.css'
})
export class AiSymptomCheckerComponent {

  message: string = "";
  messageList: string[] = [];
  isLoading: boolean = false;

  constructor(public aiService: AiService, private uiService: UiService) { }

  sendMessage() {
    // pushing message in array
    this.insertMessageinList(this.message);

    const payload: AiUserMessage = {
      message: this.message
    }
    this.isLoading = true;
    this.aiService
      .askAi(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.isLoading = false;
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          const data = response.responseBody;
          this.message = "";
          this.insertMessageinList(data.reply);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  insertMessageinList(msg: string) {
    const list = this.messageList;
    this.messageList.push(msg);
    this.messageList = [...list];
    this.scrollToBotton();
  }

  scrollToBotton(){
    const ele = document.getElementById('chatContainer');
    if (ele) {
      ele.scroll(0, document.body.scrollHeight)
    }
  }

}
