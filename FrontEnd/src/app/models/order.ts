export enum OrderStatus {
  EM_PROCESSAMENTO = 'EM_PROCESSAMENTO',
  PAUSADO = 'PAUSADO',
  CANCELADO = 'CANCELADO',
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
