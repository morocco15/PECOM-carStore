import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VoitureService } from '../service/voiture.service';

import { VoitureComponent } from './voiture.component';

describe('Voiture Management Component', () => {
  let comp: VoitureComponent;
  let fixture: ComponentFixture<VoitureComponent>;
  let service: VoitureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VoitureComponent],
    })
      .overrideTemplate(VoitureComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VoitureComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VoitureService);

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
    expect(comp.voitures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
