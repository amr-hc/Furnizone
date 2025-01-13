import { Product } from './product';
export interface Cart {
  id: number;
  user_id: number;
  product_id: number;
  quantity: number;
  product: Product | null;
}
