import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html'
})
export class AppComponent {
  message = '(loading...)';
  constructor(private http: HttpClient) {
    this.http.get<{ message: string }>('/api/hello').subscribe({
      next: res => this.message = res.message,
      error: () => this.message = 'Error calling backend'
    });
  }
}
