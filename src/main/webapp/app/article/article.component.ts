import { Component, OnInit } from '@angular/core';
import {IVoiture} from "../entities/voiture/voiture.model";
import {AccountService} from "../core/auth/account.service";
import {Router} from "@angular/router";
import {HomeService} from "../home/home.service";
import {PanierService} from "../panier/panier.service";
import {SouhaitService} from "../listedesouhait/listedesouhait.service";
import {HttpClient} from "@angular/common/http";
import {Subject} from "rxjs";
import {Account} from "../core/auth/account.model";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'jhi-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
})
export class ArticleComponent implements OnInit {

  account: Account | null = null;
  username!: string;
  voitureChoisit!: IVoiture;
  getURL!: string;
  value!:string;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private panierservice: PanierService,
    private souhaitservice: SouhaitService,
    private http: HttpClient
  ) {}

  AjouterPanier(): void {
    if (this.voitureChoisit.id != null && this.voitureChoisit.version != null) {
      this.panierservice
        .ajouterVoiturePanier(this.username, this.voitureChoisit.id, this.voitureChoisit.version)
        .subscribe((res: boolean) => {
          //eslint-disable-next-line no-console
          console.error(res);
          // eslint-disable-next-line no-console
          console.log(res);
        });
    }
  }

  AjouterSouhait(): void {
    if (this.voitureChoisit.id != null) {
      this.souhaitservice.ajouterVoitureSouhait(this.username, this.voitureChoisit.id).subscribe((res: boolean) => {
        console.error(res);
        // eslint-disable-next-line no-console
        console.log(res);
      });
    }
  }

  getValue():void{
    this.getURL = window.location.href;
    this.value = decodeURI(this.getURL.split("=")[1]);
    // eslint-disable-next-line no-console
    console.log("this.value");
    // eslint-disable-next-line no-console
    console.log(this.getURL);
  }
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    if (this.account) {
      this.username = this.account.login;
    }
    this.getValue();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
