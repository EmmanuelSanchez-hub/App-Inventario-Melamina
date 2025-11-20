import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorConexion } from './error-conexion';

describe('ErrorConexion', () => {
  let component: ErrorConexion;
  let fixture: ComponentFixture<ErrorConexion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ErrorConexion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ErrorConexion);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
