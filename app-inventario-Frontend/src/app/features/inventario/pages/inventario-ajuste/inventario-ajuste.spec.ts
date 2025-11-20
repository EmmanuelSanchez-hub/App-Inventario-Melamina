import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventarioAjuste } from './inventario-ajuste';

describe('InventarioAjuste', () => {
  let component: InventarioAjuste;
  let fixture: ComponentFixture<InventarioAjuste>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventarioAjuste]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InventarioAjuste);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
