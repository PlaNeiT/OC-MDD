import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { CommentService } from '../../../services/comment.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-read-article',
  templateUrl: './read-article.component.html',
  styleUrls: ['./read-article.component.scss']
})
export class ReadArticleComponent implements OnInit, OnDestroy {
  article: any;
  comments: any[] = [];
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
      this.loadArticle(articleId);
      this.loadComments(articleId);
    }
  }

  loadArticle(id: string): void {
    this.subscription = this.articleService.getArticle(id).subscribe(
      data => {
        this.article = data;
      },
      error => {
        console.error('Error loading article:', error);
        this.article = null;
      }
    );
  }

  loadComments(articleId: string): void {
    this.subscription = this.commentService.getCommentsForArticle(articleId).subscribe(
      data => {
        this.comments = data;
      }
    );
  }

  addComment(): void {
    if (this.newComment.trim()) {
      this.subscription = this.commentService.addComment(this.article.id, this.newComment).subscribe(() => {
        this.loadComments(this.article.id);
        this.newComment = '';
      });
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
