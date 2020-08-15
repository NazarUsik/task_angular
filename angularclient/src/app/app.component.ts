import {Component} from '@angular/core';
import {getCookie, onLogout} from './utils/utils';
import {UserService} from './service/user.service';
import {User} from './model/user';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title: string;
  user: User;
  logged: boolean;

  constructor(private router: Router,
              private userService: UserService) {
    this.logged = false;
    const login = getCookie('login');
    console.log('Home: '.concat(login));
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

  onLogout() {
    onLogout();
  }
}
