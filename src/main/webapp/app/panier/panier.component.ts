import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit {
  constructor() {
    //
  }

  ngOnInit(): void {
    //
  }

  /*test(): void{
    //var toutSelectionne = document.getElementById("toutselectionne");
    //var voiture1 = document.getElementById("voiture1");
    //var voiture2 = document.getElementById("voiture2");
    //var toutSelectionne = document.querySelector('input[value="toutselectionne"]');
    var voiture1 = document.querySelector('input[value="voiture1"]');
    var voiture2 = document.querySelector('input[value="voiture2"]');
    //if (document.querySelector('input[value="toutselectionne"]').checked === true){
    if (document.getElementById("toutselectionne").checked === true){
      voiture1.checked=true;
      voiture2.checked=true;
    }
    else{
      voiture1.checked=false;
      voiture2.checked=false;
    }
  }*/
}
