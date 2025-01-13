export interface Product {
  id: number;
  title: string;
  description: string;
  price: number;
  image: string | File;
  stock: number;
  image_url: string;
}
