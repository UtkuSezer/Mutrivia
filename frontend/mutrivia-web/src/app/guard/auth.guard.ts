import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (route.url[0].path == 'admin'){
            if (this.isAdmin(route, state)){
                return true
            }
            else{
                return false
            }
        }
        if (sessionStorage.getItem('userId')) {
            if (route.url[0].path == 'menu'){
                return true
            }
            if (route.url[0].path == 'host'){
                if (this.isHost(route, state)){
                    return true
                }
                else{
                    return false
                }
            }
            if (route.url[0].path == 'participant'){
                if (this.isParticipant(route, state)){
                    return true
                }
                else{
                    return false
                }
            }
            if (route.url[0].path == 'solo'){
                if (this.isSolo(route, state)){
                    return true
                }
                else{
                    return false
                }
            }
            if (route.url[0].path == 'gameover'){
                if (this.isHost(route, state)){
                    return true
                }
                if (this.isParticipant(route, state)){
                    return true
                }
                if (this.isSolo(route, state)){
                    return true
                }
                
                return false
                
            }
        }

        else{
            // not logged in so redirect to login page with the return url
            this.router.navigate(["register"]); 
        }
        return false;
    }

    isAdmin(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
        if (sessionStorage.getItem("isAdmin")) {
            // user is host so return true
            return true;
        }

        // not a host so redirect to login page with the return url
        this.router.navigate(["adminauth"]);
        return false;
    }

    isHost(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
        if (sessionStorage.getItem("isHost")) {
            // user is host so return true
            return true;
        }

        // not a host so redirect to login page with the return url
        this.router.navigate(["menu"]);
        return false;
    }
    
    isParticipant(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (sessionStorage.getItem("isParticipant")) {
            // user is host so return true
            return true;
        }

        // not a host so redirect to login page with the return url
        this.router.navigate(["menu"]);
        return false;
    }

    isSolo(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (sessionStorage.getItem("isSolo")) {
            // user is host so return true
            return true;
        }

        // not a host so redirect to login page with the return url
        this.router.navigate(["menu"]);
        return false;
    }
}