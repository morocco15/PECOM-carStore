import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    //const texte = document.getElementById("menuConnexion")
    //eslint-disable-next-line no-console
    //console.log(texte?.textContent);
    //texte!.textContent="Paramètres";
    //eslint-disable-next-line no-console
    /*console.log("je suis authentifié");
    if(this.accountService.isAuthenticated()){
      texte!.textContent="Paramètres";
      //eslint-disable-next-line no-console
      console.log("je suis authentifié");
    }
    else if (!this.accountService.isAuthenticated()){
      texte!.textContent="Connexion";
      //eslint-disable-next-line no-console
      console.log("je ne suis pas authentifié");
    }*/
  }

  /*ngAfterViewInit(): void {
    //eslint-disable-next-line no-console
    console.log("---ngAfterViewInit() Demo---");
    const texte = document.getElementById("menuConnexion")
    //eslint-disable-next-line no-console
    console.log(texte?.textContent);
    //texte!.textContent="Paramètres";
    //eslint-disable-next-line no-console
    console.log(texte?.textContent);
    if(this.accountService.isAuthenticated()){
      texte!.textContent="Paramètres";
      //eslint-disable-next-line no-console
      console.log("je suis authentifié");
    }
    else if (!this.accountService.isAuthenticated()){
      texte!.textContent="Connexion";
      //eslint-disable-next-line no-console
      console.log("je ne suis pas authentifié");
    }
  }*/

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
