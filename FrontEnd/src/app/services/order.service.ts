import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { Order, OrderCreate, OrderStatus } from '../models/order';
import { environment } from '../../environments/environment';
export interface OrderCreateResult {
  order: Order;
  savedLocally: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  readonly orderLimit = 5;

  private readonly apiUrl = `${environment.apiUrl}/pedidos`;
  private readonly storageKey = 'orders';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl).pipe(
      map((apiOrders) => {
        const localOrders = this.getLocalOrders();

        return [...apiOrders, ...localOrders];
      }),

      catchError((error: HttpErrorResponse) => {
        if (this.isApiUnavailable(error)) {
          return of(this.getLocalOrders());
        }

        return throwError(() => error);
      }),
    );
  }

  getById(id: string): Observable<Order> {
    if (this.isLocalId(id)) {
      const localOrder = this.getLocalOrders().find((order) => order.id === id);

      if (!localOrder) {
        return throwError(() => new Error('Pedido local não encontrado.'));
      }

      return of(localOrder);
    }

    return this.http.get<Order>(`${this.apiUrl}/${id}`);
  }

  create(data: OrderCreate): Observable<OrderCreateResult> {
    return this.http.post<Order>(this.apiUrl, data).pipe(
      map((order) => ({
        order,
        savedLocally: false,
      })),

      catchError((error: HttpErrorResponse) => {
        if (!this.isApiUnavailable(error)) {
          return throwError(() => error);
        }

        try {
          const order = this.saveLocalOrder(data);

          return of({
            order,
            savedLocally: true,
          });
        } catch (storageError) {
          return throwError(() => storageError);
        }
      }),
    );
  }

  updateStatus(id: string, status: OrderStatus): Observable<Order> {
    if (this.isLocalId(id)) {
      try {
        return of(this.updateLocalOrderStatus(id, status));
      } catch (error) {
        return throwError(() => error);
      }
    }

    return this.http.patch<Order>(`${this.apiUrl}/${id}/status`, {
      status,
    });
  }

  delete(id: string): Observable<void> {
    if (this.isLocalId(id)) {
      try {
        this.deleteLocalOrder(id);
        return of(undefined);
      } catch (error) {
        return throwError(() => error);
      }
    }

    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getLocalOrders(): Order[] {
    if (!this.isLocalStorageAvailable()) {
      return [];
    }

    try {
      const storedOrders = localStorage.getItem(this.storageKey);

      if (!storedOrders) {
        return [];
      }

      const parsedOrders: unknown = JSON.parse(storedOrders);

      if (!Array.isArray(parsedOrders)) {
        return [];
      }

      return parsedOrders as Order[];
    } catch {
      return [];
    }
  }

  private saveLocalOrder(data: OrderCreate): Order {
    const orders = this.getLocalOrders();

    if (orders.length >= this.orderLimit) {
      throw new Error(
        `O limite máximo de ${this.orderLimit} pedidos foi atingido.`,
      );
    }

    const order: Order = {
      id: this.generateLocalId(),
      displayName: data.displayName,
      weight: data.weight,
      items: data.items,
      status: OrderStatus.EM_PROCESSAMENTO,
    };

    this.setLocalOrders([...orders, order]);

    return order;
  }

  private updateLocalOrderStatus(id: string, newStatus: OrderStatus): Order {
    const orders = this.getLocalOrders();

    const orderIndex = orders.findIndex((order) => order.id === id);

    if (orderIndex === -1) {
      throw new Error('Pedido local não encontrado.');
    }

    const currentOrder = orders[orderIndex];

    if (!this.isValidStatusTransition(currentOrder.status, newStatus)) {
      throw new Error(
        `Transição de ${currentOrder.status} para ${newStatus} não permitida.`,
      );
    }

    const updatedOrder: Order = {
      ...currentOrder,
      status: newStatus,
    };

    const updatedOrders = [...orders];
    updatedOrders[orderIndex] = updatedOrder;

    this.setLocalOrders(updatedOrders);

    return updatedOrder;
  }

  private deleteLocalOrder(id: string): void {
    const orders = this.getLocalOrders();

    const orderExists = orders.some((order) => order.id === id);

    if (!orderExists) {
      throw new Error('Pedido local não encontrado.');
    }

    const updatedOrders = orders.filter((order) => order.id !== id);

    this.setLocalOrders(updatedOrders);
  }

  private setLocalOrders(orders: Order[]): void {
    if (!this.isLocalStorageAvailable()) {
      throw new Error(
        'O armazenamento local não está disponível neste navegador.',
      );
    }

    localStorage.setItem(this.storageKey, JSON.stringify(orders));
  }

  private generateLocalId(): string {
    if (
      typeof crypto !== 'undefined' &&
      typeof crypto.randomUUID === 'function'
    ) {
      return `local-${crypto.randomUUID()}`;
    }

    return `local-${Date.now()}`;
  }

  private isLocalId(id: string): boolean {
    return id.startsWith('local-');
  }

  private isLocalStorageAvailable(): boolean {
    return typeof localStorage !== 'undefined';
  }

  private isApiUnavailable(error: HttpErrorResponse): boolean {
    return [0, 502, 503, 504].includes(error.status);
  }

  private isValidStatusTransition(
    currentStatus: OrderStatus,
    newStatus: OrderStatus,
  ): boolean {
    
    const allowedTransitions: Record<OrderStatus, OrderStatus[]> = {
  [OrderStatus.EM_PROCESSAMENTO]: [
    OrderStatus.PAUSADO,
    OrderStatus.CANCELADO,
  ],

  [OrderStatus.PAUSADO]: [
    OrderStatus.EM_PROCESSAMENTO,
    OrderStatus.CANCELADO,
  ],

  [OrderStatus.CANCELADO]: [
    OrderStatus.EM_PROCESSAMENTO,
  ],
};

    return allowedTransitions[currentStatus].includes(newStatus);
  }
}
