import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {User} from '../model/user';
import {getCookie} from '../utils/utils';
import {sourceInfo} from '@angular/compiler-cli/src/metadata/evaluator';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {

  user: User;
  login: string;
  roleId: string;
  invalid: boolean;
  registration: boolean;
  levels: Array<Object> = [
    {num: 1, name: 'Admin'},
    {num: 2, name: 'User'}
  ];
  errors = {
    login: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    birthday: '',
    roleId: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {
    this.user = new User();
    this.invalid = false;
    const login = getCookie('login');
    this.registration = login === null || login === undefined || login === '';
    this.route.queryParams
      .subscribe(params => {
        this.login = params['login'];
      });
    if (this.login != null) {
      if (login === '') {
        this.router.navigate(['/login']);
      }
      userService.read(this.login).subscribe(data => {
        this.user = data;
        this.user.password = '';
        // tslint:disable-next-line:radix
        this.user.birthday = new Date(parseInt(this.user.birthday.toString()));
        this.roleId = this.user.roleId;
      });
    } else {
      this.user = new User();
      this.user.roleId = '2';
      this.roleId = String('2');
    }
  }

  toRoleId() {
    this.user.roleId = this.roleId;
  }

  onSubmit() {
    this.validate(this.user);
    if (this.login == null) {
      console.log(this.user.roleId);
      this.userService.create(this.user).subscribe(
        result => {
          if (result.status === 201) {
            this.router.navigate(['/']);
          } else {
            this.invalid = true;
          }
        },
        error => this.invalid = true);
    } else {
      this.userService.update(this.user).subscribe(
        result => {
          if (result.status === 200) {
            this.router.navigate(['/']);
          } else {
            this.invalid = true;
          }
        },
        error => this.invalid = true);
    }
  }

  validate(values) {
    if (!values.login) {
      this.errors.login = 'Enter a Login';
    }

    if (!values.email) {
      this.errors.email = 'Enter a Email';
    }

    if (!values.password) {
      this.errors.password = 'Enter a Password';
    } else if (values.password.length < 8) {
      this.errors.password = 'Enter at least 8 Characters in Password';
    }

    if (!values.firstName) {
      this.errors.firstName = 'Enter a First Name';
    } else if (values.firstName.length <= 1) {
      this.errors.firstName = 'Enter at least 1 Characters in First Name';
    }

    if (!values.lastName) {
      this.errors.lastName = 'Enter a Last Name';
    } else if (values.lastName.length <= 1) {
      this.errors.lastName = 'Enter at least 1 Characters in Last Name';
    }
  }

}
