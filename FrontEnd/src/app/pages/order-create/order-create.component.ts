import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import { OrderCreate } from '../../models/order';
import { OrderService } from '../../services/order.service';
import { ButtonComponent } from '../../shared/ui/button/button.component';

@Component({
  selector: 'app-order-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
  ],
  templateUrl: './order-create.component.html',
  styleUrl: './order-create.component.scss',
})
export class OrderCreateComponent implements OnInit {
  readonly orderLimit = this.orderService.orderLimit;

  readonly orderForm = this.formBuilder.nonNullable.group({
    client: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
      ],
    ],
    weight: [
      0,
      [
        Validators.required,
        Validators.min(0.001),
      ],
    ],
    items: [
      1,
      [
        Validators.required,
        Validators.min(1),
        this.integerValidator,
      ],
    ],
  });

  loading = false;
  reachedLimit = false;
  errorMessage = '';

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly orderService: OrderService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.checkOrderLimit();
  }

  submitOrder(): void {
    if (this.loading) {
      return;
    }

    this.errorMessage = '';

    if (this.reachedLimit) {
      this.errorMessage =
        `O limite máximo de ${this.orderLimit} pedidos foi atingido.`;

      return;
    }

    if (this.orderForm.invalid) {
      this.orderForm.markAllAsTouched();
      return;
    }

    const formValue = this.orderForm.getRawValue();
    const clientName = formValue.client.trim();

    if (clientName.length < 5) {
      this.orderForm.controls.client.setErrors({
        minlength: true,
      });

      this.orderForm.controls.client.markAsTouched();
      return;
    }

    const data: OrderCreate = {
      displayName: clientName,
      weight: this.convertKgToGrams(formValue.weight),
      items: formValue.items,
    };

    this.loading = true;

    this.orderService
      .create(data)
      .pipe(
        finalize(() => {
          this.loading = false;
        }),
      )
      .subscribe({
        next: (result) => {
          const message = result.savedLocally
            ? 'API indisponível. O pedido foi salvo localmente.'
            : 'Pedido cadastrado com sucesso.';

          void this.router.navigate(['/orders'], {
            state: { message },
          });
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message ??
            error.message ??
            'Não foi possível cadastrar o pedido.';

          if (error.error?.code === 'ORDER_LIMIT_REACHED') {
            this.reachedLimit = true;
          }
        },
      });
  }

  goBack(): void {
    void this.router.navigate(['/orders']);
  }

  isInvalid(fieldName: string): boolean {
    const field = this.orderForm.get(fieldName);

    return Boolean(
      field?.invalid &&
      (field.dirty || field.touched),
    );
  }

  hasError(
    fieldName: string,
    errorName: string,
  ): boolean {
    const field = this.orderForm.get(fieldName);

    return Boolean(
      field?.hasError(errorName) &&
      (field.dirty || field.touched),
    );
  }

  private checkOrderLimit(): void {
    this.orderService.getAll().subscribe({
      next: (orders) => {
        this.reachedLimit =
          orders.length >= this.orderLimit;
      },
      error: () => {
        this.errorMessage =
          'Não foi possível verificar o limite de pedidos.';
      },
    });
  }

  private convertKgToGrams(
    weightInKg: number,
  ): number {
    return Math.round(weightInKg * 1000);
  }

  private integerValidator(
    control: AbstractControl,
  ): ValidationErrors | null {
    const value = control.value;

    if (
      value === null ||
      value === undefined ||
      value === ''
    ) {
      return null;
    }

    return Number.isInteger(Number(value))
      ? null
      : { integer: true };
  }
}