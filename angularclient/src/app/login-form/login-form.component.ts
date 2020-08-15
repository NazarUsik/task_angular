import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {setCookie} from '../utils/utils';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {

  login: string;
  password: string;
  invalid: boolean;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) {
    this.invalid = false;
  }

  onSubmit() {
    this.userService.login(this.login, this.password).subscribe(response => {
      if (response.status === 200) {
        setCookie('login', this.login);
        console.log('Login user: '.concat(this.login));
        this.gotoUserList();
      } else {
        this.invalid = true;
      }
    }, error => {
      this.invalid = true;
    });
  }

  gotoUserList() {
    this.router.navigate(['/']);
    window.location.reload();
  }
}
