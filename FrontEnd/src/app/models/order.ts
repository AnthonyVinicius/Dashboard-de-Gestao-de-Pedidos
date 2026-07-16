export enum OrderStatus {
  PROCESSING = 'PROCESSING',
  PAUSED = 'PAUSED',
  CANCELED = 'CANCELED',
}


export interface Order {
  id: string;
  displayName: string;
  weight: number;
  items: number;
  status: OrderStatus;
}

export interface OrderCreate {
  displayName: string;
  weight: number;
  items: number;
}