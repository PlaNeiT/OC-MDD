export interface ArticleDTO {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  user: { username: string };
  theme: { username: string };
}
