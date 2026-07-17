import { CommonModule, DecimalPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { finalize } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { Order, OrderStatus } from '../../models/order';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    DecimalPipe, 
    RouterModule,
    MatCardModule, 
    MatButtonModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatSelectModule, 
    MatProgressSpinnerModule
  ],
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  readonly OrderStatus = OrderStatus;
  readonly orderLimit = this.orderService.orderLimit;
  orders: Order[] = [];

  search = '';
  selectedStatus: OrderStatus | '' = '';

  loading = false;
  message = '';
  errorMessage = '';

  processingOrderId: string | null = null;

  constructor(
    private readonly orderService: OrderService,
    private readonly router: Router,
  ) {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state;

    if (state?.['message']) {
      this.message = state['message'];
    }
  }

  ngOnInit(): void {
    this.loadOrders();

    if (this.message) {
      this.clearMessageAfterDelay();
    }
  }

  get reachedLimit(): boolean {
    return this.orders.length >= this.orderLimit;
  }

  get filteredOrders(): Order[] {
    const normalizedSearch = this.search.trim().toLowerCase();

    return this.orders.filter((order) => {
      const matchesName = order.displayName
        .toLowerCase()
        .includes(normalizedSearch);

      const matchesStatus =
        !this.selectedStatus || order.status === this.selectedStatus;

      return matchesName && matchesStatus;
    });
  }

  loadOrders(): void {
    this.loading = true;
    this.errorMessage = '';

    this.orderService
      .getAll()
      .pipe(
        finalize(() => {
          this.loading = false;
        }),
      )
      .subscribe({
        next: (orders) => {
          this.orders = orders;
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message ??
            error.message ??
            'Não foi possível carregar os pedidos.';
        },
      });
  }

  addOrder(): void {
    if (this.reachedLimit) {
      this.showMessage(
        `O limite máximo de ${this.orderLimit} pedidos foi atingido.`,
      );
      return;
    }
    void this.router.navigate(['/order/create']);
  }

  updateStatus(order: Order, newStatus: OrderStatus): void {
    this.errorMessage = '';

    if (this.processingOrderId) {
      return;
    }

    if (!this.canChangeTo(order.status, newStatus)) {
      this.showMessage('Transição de status não permitida.');
      return;
    }

    this.processingOrderId = order.id;

    this.orderService
      .updateStatus(order.id, newStatus)
      .pipe(
        finalize(() => {
          this.processingOrderId = null;
        }),
      )
      .subscribe({
        next: (updatedOrder) => {
          this.orders = this.orders.map((currentOrder) =>
            currentOrder.id === updatedOrder.id ? updatedOrder : currentOrder,
          );
          this.showMessage('Status updated successfully.');
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message ??
            error.message ??
            'Não foi possível atualizar o status.';
        },
      });
  }

  deleteOrder(order: Order): void {
    this.errorMessage = '';

    if (this.processingOrderId) {
      return;
    }

    const confirmed = window.confirm(
      `Deseja realmente excluir o pedido de ${order.displayName}?`,
    );

    if (!confirmed) {
      return;
    }

    this.processingOrderId = order.id;

    this.orderService
      .delete(order.id)
      .pipe(
        finalize(() => {
          this.processingOrderId = null;
        }),
      )
      .subscribe({
        next: () => {
          this.orders = this.orders.filter(
            (currentOrder) => currentOrder.id !== order.id,
          );
          this.showMessage('Pedido excluído com sucesso.');
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message ??
            error.message ??
            'Não foi possível excluir o pedido.';
        },
      });
  }

  canChangeTo(currentStatus: OrderStatus, newStatus: OrderStatus): boolean {
    const transitions: Record<OrderStatus, OrderStatus[]> = {
      [OrderStatus.EM_PROCESSAMENTO]: [OrderStatus.PAUSADO, OrderStatus.CANCELADO],
      [OrderStatus.PAUSADO]: [OrderStatus.EM_PROCESSAMENTO, OrderStatus.CANCELADO],
      [OrderStatus.CANCELADO]: [OrderStatus.EM_PROCESSAMENTO],
    };
    return transitions[currentStatus].includes(newStatus);
  }

  formatStatus(status: OrderStatus): string {
    const labels: Record<OrderStatus, string> = {
      [OrderStatus.EM_PROCESSAMENTO]: 'Em processamento',
      [OrderStatus.PAUSADO]: 'Pausado',
      [OrderStatus.CANCELADO]: 'Cancelado',
    };
    return labels[status];
  }

  weightInKg(weight: number): number {
    return weight / 1000;
  }

  private showMessage(message: string): void {
    this.message = message;
    this.clearMessageAfterDelay();
  }

  private clearMessageAfterDelay(): void {
    window.setTimeout(() => {
      this.message = '';
    }, 3000);
  }
}