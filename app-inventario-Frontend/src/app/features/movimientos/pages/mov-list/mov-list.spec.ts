import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovList } from './mov-list';

describe('MovList', () => {
  let component: MovList;
  let fixture: ComponentFixture<MovList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
