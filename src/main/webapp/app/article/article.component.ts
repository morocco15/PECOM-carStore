import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
})
export class ArticleComponent implements OnInit {
  click_account = 0;



  constructor() {
    //
  }

  ngOnInit(): void {
    //
  }

  //fonction pour l'image change
  nextSlide():void
  {
     this.click_account = (this.click_account + 1) % 3;
  }

   //fonction pour l'image change
  preSlide():void
  {
     if(this.click_account === 0)
     {
       this.click_account = 3;
     }
     this.click_account = (this.click_account - 1) % 3;
  }
}
