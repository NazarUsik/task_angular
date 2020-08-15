import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaderResponse} from '@angular/common/http';
import {User} from '../model/user';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UserService {

  usersUrl: string;

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
}
