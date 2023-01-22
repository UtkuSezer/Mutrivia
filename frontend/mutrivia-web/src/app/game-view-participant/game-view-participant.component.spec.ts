import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameViewParticipantComponent } from './game-view-participant.component';

describe('GameViewParticipantComponent', () => {
  let component: GameViewParticipantComponent;
  let fixture: ComponentFixture<GameViewParticipantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameViewParticipantComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameViewParticipantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
