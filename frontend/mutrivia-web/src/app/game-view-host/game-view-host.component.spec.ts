import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameViewHostComponent } from './game-view-host.component';

describe('GameViewHostComponent', () => {
  let component: GameViewHostComponent;
  let fixture: ComponentFixture<GameViewHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameViewHostComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameViewHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
