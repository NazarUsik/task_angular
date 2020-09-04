import {Injectable} from '@angular/core';

declare var Keycloak: any;

@Injectable()
export class KeycloakService {
    static auth: any = {};

    constructor() {
    }

    init(): Promise<any> {
        return new Promise((resolve, reject) => {
            const keycloakAuth = new Keycloak('assets/keycloak/keycloak.json');
            keycloakAuth.init({onLoad: 'login-required'})
                .then(() => {
                    KeycloakService.auth.loggedIn = true;
                    KeycloakService.auth.keycloak = keycloakAuth;
                    KeycloakService.auth.logoutUrl = keycloakAuth.authServerUrl
                        + 'realms/AngularKeycloak/protocol/openid-connect/logout?redirect_uri='
                        + document.baseURI;
                    KeycloakService.auth.addUserUrl = keycloakAuth.authServerUrl
                        + 'admin/realms/AngularKeycloak/users';
                    console.log(KeycloakService.auth);
                    resolve();
                })
                .catch((error) => {
                    console.log(error);
                    reject();
                });
        });
    }

    getToken(): Promise<string> {
        return new Promise<string>(((resolve, reject) => {
            if (KeycloakService.auth.keycloak.token) {
                KeycloakService.auth.keycloak
                    .updateToken(60)
                    .then((refreshed) => {
                        console.log('refreshed '.concat(refreshed));
                        resolve(KeycloakService.auth.keycloak.token as string);
                    })
                    .catch(() => {
                        reject('Failed to refresh token');
                    });
            } else {
                reject('Not logged in');
            }
        }));
    }

    getParsedToken() {
        return KeycloakService.auth.keycloak.tokenParsed;
    }
}
