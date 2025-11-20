import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovSalida } from './mov-salida';

describe('MovSalida', () => {
  let component: MovSalida;
  let fixture: ComponentFixture<MovSalida>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovSalida]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovSalida);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
