import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonComponent } from "../../shared/ui/button/button.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ButtonComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {}