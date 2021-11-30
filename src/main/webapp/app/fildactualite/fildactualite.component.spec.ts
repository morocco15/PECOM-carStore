import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FildactualiteComponent } from './fildactualite.component';

describe('FildactualiteComponent', () => {
  let component: FildactualiteComponent;
  let fixture: ComponentFixture<FildactualiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FildactualiteComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FildactualiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
