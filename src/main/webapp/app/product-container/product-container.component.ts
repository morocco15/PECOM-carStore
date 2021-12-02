import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { IVoiture } from 'app/entities/voiture/voiture.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ProductContainerService } from './product-container.service';

@Component({
  selector: 'jhi-product-container',
  templateUrl: './product-container.component.html',
  styleUrls: ['./product-container.component.scss']
})
export class ProductContainerComponent implements OnInit {

  account: Account | null = null;
  voiture!: IVoiture;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private containerservice: ProductContainerService , private http: HttpClient)
  {
    //
  }

  callService(): void {
    this.containerservice.getVoitures(1).subscribe((res: IVoiture) => {
      //eslint-disable-next-line no-console
      console.error(res);
      this.voiture = res;

    });
  }

  ngOnInit(): void
  {
    //
    this.accountService.getAuthenticationState().pipe(takeUntil(this.destroy$))
    .subscribe(account => (this.account = account));
    this.callService();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  handlePay():void
  {
    //

  }

}
