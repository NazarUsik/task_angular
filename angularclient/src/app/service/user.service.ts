import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../model/user';
import {Observable} from 'rxjs';
import {KeycloakService} from './keycloak.service';

@Injectable()
export class UserService {

    private readonly usersUrl: string;

    constructor(private http: HttpClient) {
        this.usersUrl = 'http://localhost:8080/user';
    }

    public create(user: User) {
        return this.http.post(
            this.usersUrl,
            user,
            {observe: 'response', responseType: 'text'});
    }

    public readAll(): Observable<User[]> {
        return this.http.get<User[]>(this.usersUrl);
    }

    public read(login: string) {
        console.log('method read user in back');
        return this.http.get<User>(this.usersUrl.concat('/').concat(login));
    }

    public update(user: User) {
        return this.http.put(
            this.usersUrl,
            user,
            {observe: 'response', responseType: 'text'});
    }

    public delete(login: string) {
        return this.http.delete(
            this.usersUrl.concat('/').concat(login),
            {observe: 'response', responseType: 'text'});
    }

    public login(login: string, password: string) {
        return this.http.post(
            'http://localhost:8080/'
                .concat('j_spring_security_check?')
                .concat('j_username=')
                .concat(login)
                .concat('&j_password=')
                .concat(password),
            null,
            {observe: 'response', responseType: 'text'});
    }

    public addUserToKeycloak(usname: string, pass: string) {
        const head = new HttpHeaders()
            .set('Authorization', 'Bearer '.concat(KeycloakService.auth.keycloak.token))
            .set('Content-Type', 'application/json');
        return this.http.post(KeycloakService.auth.addUserUrl,
            {
                enabled: true,
                username: usname
            },
            {observe: 'response', responseType: 'text', headers: head});
    }
}
