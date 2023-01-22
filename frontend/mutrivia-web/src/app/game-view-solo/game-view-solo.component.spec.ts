import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameViewSoloComponent } from './game-view-solo.component';

describe('GameViewSoloComponent', () => {
  let component: GameViewSoloComponent;
  let fixture: ComponentFixture<GameViewSoloComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameViewSoloComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameViewSoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
