import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { RegisterComponent } from './register/register.component';
import { MenuComponent } from './menu/menu.component';
import { ErrorComponent } from './error/error.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { GameViewHostComponent } from './game-view-host/game-view-host.component';
import { GameViewParticipantComponent } from './game-view-participant/game-view-participant.component';
import { GameViewSoloComponent } from './game-view-solo/game-view-solo.component';
import { AuthGuard } from './guard/auth.guard';
import { GameoverComponent } from './gameover/gameover.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    RegisterComponent,
    MenuComponent,
    ErrorComponent,
    GameViewHostComponent,
    GameViewParticipantComponent,
    GameViewSoloComponent,
    GameoverComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    AuthGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
