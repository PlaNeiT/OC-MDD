export interface ArticleDTO {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  author: { name: string };
  theme: { name: string };
}
