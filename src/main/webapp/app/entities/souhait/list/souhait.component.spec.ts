import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SouhaitService } from '../service/souhait.service';

import { SouhaitComponent } from './souhait.component';

describe('Souhait Management Component', () => {
  let comp: SouhaitComponent;
  let fixture: ComponentFixture<SouhaitComponent>;
  let service: SouhaitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SouhaitComponent],
    })
      .overrideTemplate(SouhaitComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SouhaitComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SouhaitService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.souhaits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
