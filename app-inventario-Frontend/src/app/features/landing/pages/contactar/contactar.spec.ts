import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Contactar } from './contactar';

describe('Contactar', () => {
  let component: Contactar;
  let fixture: ComponentFixture<Contactar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Contactar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Contactar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
