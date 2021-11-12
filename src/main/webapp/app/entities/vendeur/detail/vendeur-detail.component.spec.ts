import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VendeurDetailComponent } from './vendeur-detail.component';

describe('Vendeur Management Detail Component', () => {
  let comp: VendeurDetailComponent;
  let fixture: ComponentFixture<VendeurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VendeurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vendeur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VendeurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VendeurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vendeur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vendeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
