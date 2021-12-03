import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VoitureDetailComponent } from './voiture-detail.component';

describe('Voiture Management Detail Component', () => {
  let comp: VoitureDetailComponent;
  let fixture: ComponentFixture<VoitureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VoitureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ voiture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VoitureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VoitureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load voiture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.voiture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
