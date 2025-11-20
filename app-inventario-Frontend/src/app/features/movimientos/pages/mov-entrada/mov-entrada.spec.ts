import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovEntrada } from './mov-entrada';

describe('MovEntrada', () => {
  let component: MovEntrada;
  let fixture: ComponentFixture<MovEntrada>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovEntrada]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovEntrada);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
