export interface ArticleDTO {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  user: {
    id: number;
    username: string;
  };
  theme: {
    id: number;
    name: string;
  };
}
