import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { ARTICLE_ROUTE } from './article.route';
import { ArticleComponent } from './article.component';
import { ArticleService } from './article.service';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([ARTICLE_ROUTE])],
  declarations: [ArticleComponent],
  providers: [ArticleService],
})
export class ArticleModule {}
