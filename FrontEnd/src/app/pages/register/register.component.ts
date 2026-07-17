import { Component } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { ButtonComponent } from '../../shared/ui/button/button.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    RouterLink,
    ButtonComponent,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {

  loading = false;
  errorMessage = '';

  registerForm = this.formBuilder.nonNullable.group({
    name: ['', [
      Validators.required,
      Validators.minLength(3)
    ]],
    email: ['', [
      Validators.required,
      Validators.email
    ]],
    password: ['', [
      Validators.required,
      Validators.minLength(8)
    ]]
  });

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  submit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService
      .register(this.registerForm.getRawValue())
      .subscribe({
        next: () => {
          this.loading = false;
          void this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage =
            error.error?.message ??
            'Não foi possível cadastrar o usuário.';
        }
      });
  }
}