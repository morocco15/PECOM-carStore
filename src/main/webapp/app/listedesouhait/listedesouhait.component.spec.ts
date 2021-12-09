import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListedesouhaitComponent } from './listedesouhait.component';

describe('ListedesouhaitComponent', () => {
  let component: ListedesouhaitComponent;
  let fixture: ComponentFixture<ListedesouhaitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListedesouhaitComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListedesouhaitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
