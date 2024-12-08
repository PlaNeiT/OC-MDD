import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { CommentService } from '../../../services/comment.service';
import { Subscription } from 'rxjs';
import { ArticleDTO } from '../../../dtos/article-dto';
import { CommentDTO } from '../../../dtos/comment-dto';

@Component({
  selector: 'app-read-article',
  templateUrl: './read-article.component.html',
  styleUrls: ['./read-article.component.scss']
})
export class ReadArticleComponent implements OnInit, OnDestroy {
  article: ArticleDTO | null = null;
  comments: CommentDTO[] = [];
  newComment: string = '';
  subscription: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    const articleId = this.route.snapshot.paramMap.get('id');
    if (articleId) {
      const id = Number(articleId);
      if (!isNaN(id)) {
        this.loadArticle(id);
        this.loadComments(id);
      } else {
        console.error('Article ID is not valid');
      }
    }
  }

  loadArticle(id: number): void {
    this.subscription = this.articleService.getArticle(id).subscribe(
      (data: ArticleDTO) => {
        this.article = data;
      },
      (error) => {
        console.error('Error loading article:', error);
        this.article = null;
      }
    );
  }

  loadComments(articleId: number): void {
    this.subscription = this.commentService.getCommentsForArticle(articleId).subscribe(
      (data: CommentDTO[]) => {
        this.comments = data;
      }
    );
  }

  addComment(): void {
    if (this.newComment.trim() && this.article) {
      this.subscription = this.commentService.addComment(this.article.id, this.newComment).subscribe(() => {
        if (this.article) {
          this.loadComments(this.article.id);
        }
        this.newComment = '';
      });
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
