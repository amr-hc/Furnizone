export interface Admin {
  id?: number;
  name: string;
  email: string;
  image?: string | File;
  image_url: string;
  password?: string;
}
