import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TarjetaInventario } from './tarjeta-inventario';

describe('TarjetaInventario', () => {
  let component: TarjetaInventario;
  let fixture: ComponentFixture<TarjetaInventario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TarjetaInventario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TarjetaInventario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
