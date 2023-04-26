import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './error/error.component';
import { GameViewHostComponent } from './game-view-host/game-view-host.component';
import { GameViewParticipantComponent } from './game-view-participant/game-view-participant.component';
import { GameViewSoloComponent } from './game-view-solo/game-view-solo.component';
import { GameoverComponent } from './gameover/gameover.component';
import { AuthGuard } from './guard/auth.guard';
import { MenuComponent } from './menu/menu.component';
import { RegisterComponent } from './register/register.component';
import { AdminAuthComponent } from './admin-auth/admin-auth.component';
import { AdminComponent } from './admin/admin.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

const routes: Routes = [
  {path:'', component:RegisterComponent},
  {path:'register', component:RegisterComponent},
  {path:'menu', component:MenuComponent, canActivate: [AuthGuard]},
  {path:'host', component:GameViewHostComponent, canActivate: [AuthGuard]},
  {path:'participant', component:GameViewParticipantComponent, canActivate: [AuthGuard]},
  {path:'solo', component:GameViewSoloComponent, canActivate: [AuthGuard]},
  {path:'gameover', component:GameoverComponent},
  {path:'leaderboard', component:LeaderboardComponent},
  {path:'adminauth', component:AdminAuthComponent},
  {path:'admin', component:AdminComponent, canActivate: [AuthGuard]},
  {path:'**', component:ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
