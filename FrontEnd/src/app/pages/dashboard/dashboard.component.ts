import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { interval, startWith, Subject, switchMap, takeUntil } from 'rxjs';
import { ButtonComponent } from '../../shared/ui/button/button.component';
import { Order, OrderStatus } from '../../models/order';
import { OrderService } from '../../services/order.service';
import { environment } from '../../../environments/environment';
import { RouterLink } from '@angular/router';
interface StatusSummary {
  name: string;
  value: number;
  cssClass: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ButtonComponent, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  readonly orderLimit = this.orderService.orderLimit;
  private readonly healthUrl = `${environment.actuatorUrl}/health`;
  orders: Order[] = [];

  apiOnline = false;
  loading = false;
  errorMessage = '';

  private readonly destroy$ = new Subject<void>();

  constructor(private readonly orderService: OrderService,private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.startOrdersPolling();
    this.startApiHealthPolling();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get totalOrders(): number {return this.orders.length;}

  get processingOrders(): number {return this.countByStatus(OrderStatus.EM_PROCESSAMENTO);}

  get pausedOrders(): number {return this.countByStatus(OrderStatus.PAUSADO);}

  get canceledOrders(): number {return this.countByStatus(OrderStatus.CANCELADO);}

  get totalItems(): number {return this.orders.reduce((total, order) => total + order.items, 0);}

  get totalWeightKg(): number {
    const totalInGrams = this.orders.reduce((total, order) => total + order.weight,0);
    return totalInGrams / 1000;
  }

  get limitPercentage(): number {return Math.min((this.totalOrders / this.orderLimit) * 100, 100);}

  get remainingOrders(): number {return Math.max(this.orderLimit - this.totalOrders, 0);}

  get statuses(): StatusSummary[] {
    return [
      {
        name: 'Em processamento',
        value: this.processingOrders,
        cssClass: 'em_processamento',
      },
      {
        name: 'Pausados',
        value: this.pausedOrders,
        cssClass: 'pausados',
      },
      {
        name: 'Cancelados',
        value: this.canceledOrders,
        cssClass: 'cancelados',
      },
    ];
  }

  getBarHeight(value: number): number {
    if (this.totalOrders === 0 || value === 0) {
      return 0;
    }
    return Math.max((value / this.totalOrders) * 100, 8);
  }

  private startOrdersPolling(): void {
    this.loading = true;

    interval(10000)
      .pipe(
        startWith(0),
        switchMap(() => this.orderService.getAll()),
        takeUntil(this.destroy$),
      )
      .subscribe({
        next: (orders) => {
          this.orders = orders;
          this.loading = false;
          this.errorMessage = '';
        },
        error: (error) => {
          this.loading = false;

          this.errorMessage =
            error.error?.message ??
            error.message ??
            'Não foi possível carregar os pedidos.';
        },
      });
  }

  private startApiHealthPolling(): void {
    interval(10000)
      .pipe(
        startWith(0),
        switchMap(() => this.http.get(this.healthUrl)),
        takeUntil(this.destroy$),
      )
      .subscribe({
        next: () => {
          this.apiOnline = true;
        },
        error: () => {
          this.apiOnline = false;
        },
      });
  }

  private countByStatus(status: OrderStatus): number {
    return this.orders.filter((order) => order.status === status).length;
  }
}
