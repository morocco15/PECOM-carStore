/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ProductContainerService } from './product-container.service';

describe('Service: ProductContainer', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProductContainerService]
    });
  });

  it('should ...', inject([ProductContainerService], (service: ProductContainerService) => {
    expect(service).toBeTruthy();
  }));
});
