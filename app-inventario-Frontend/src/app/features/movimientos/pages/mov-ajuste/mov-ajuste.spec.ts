import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovAjuste } from './mov-ajuste';

describe('MovAjuste', () => {
  let component: MovAjuste;
  let fixture: ComponentFixture<MovAjuste>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovAjuste]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovAjuste);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
