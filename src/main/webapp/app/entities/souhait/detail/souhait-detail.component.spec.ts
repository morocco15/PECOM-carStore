import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SouhaitDetailComponent } from './souhait-detail.component';

describe('Souhait Management Detail Component', () => {
  let comp: SouhaitDetailComponent;
  let fixture: ComponentFixture<SouhaitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SouhaitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ souhait: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SouhaitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SouhaitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load souhait on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.souhait).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
