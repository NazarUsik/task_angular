import {Component, OnInit} from '@angular/core';
import {getCookie, onLogout, setCookie} from './utils/utils';
import {UserService} from './service/user.service';
import {User} from './model/user';
import {Router} from '@angular/router';
import {KeycloakService} from './service/keycloak.service';
import {HttpClient} from '@angular/common/http';


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
    title: string;
    user: User;
    logged: boolean;

    constructor(private router: Router,
                private userService: UserService,
                private keycloak: KeycloakService) {
        this.logged = false;
        const login = keycloak.getParsedToken().preferred_username;
        setCookie('login', login);
        if (
            this.user === undefined &&
            login !== undefined &&
            login !== ''
        ) {
            this.title = 'Hello, '.concat(login).concat('!');
            this.userService.read(login).subscribe(result => {
                this.user = result;
                this.logged = true;
                this.router.navigate(['/']);
            }, error => {
                console.log('error to rear user');
                console.log('error: '.concat(error));
            });
        } else if (this.user !== undefined) {
            this.logged = true;
            this.title = 'Hello, '.concat(this.user.login).concat('!');
        } else {
            this.title = 'Hello!';
            this.user = new User();
            this.user.roleId = '-1';
        }
    }

    ngOnInit() {
    }


    onLogout() {
        KeycloakService.auth.logged = false;
        KeycloakService.auth.authz = null;
        onLogout();
        window.location.href = KeycloakService.auth.logoutUrl;
    }
}
