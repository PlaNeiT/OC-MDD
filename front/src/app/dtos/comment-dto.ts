export interface CommentDTO {
  id: number;
  content: string;
  createdAt: string;
  user: { username: string };
}
