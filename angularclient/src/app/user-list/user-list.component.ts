import {Component, OnInit} from '@angular/core';
import {User} from '../model/user';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';
import {getCookie, onLogout} from '../utils/utils';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];

  constructor(
    private router: Router,
    private userService: UserService
  ) {
  }

  ngOnInit() {
    const login = getCookie('login');
    if (login === null || login === undefined || login === '') {
      this.router.navigate(['/login']);
    }
    this.userService.read(login).subscribe(result => {
      // tslint:disable-next-line:triple-equals
      if (result.roleId == '1') {
        this.userService.readAll().subscribe(data => {
          this.users = data;
          this.users.forEach(user => {
            // tslint:disable-next-line:radix
            user.birthday = new Date(parseInt(user.birthday.toString()));
          });
        });
      }
    });
  }

  delete(login: string) {
    const log = getCookie('login');
    if (login === log) {
      this.userService.delete(login).subscribe(result => onLogout());
    } else {
      this.userService.delete(login).subscribe(result => this.ngOnInit());
    }
  }
}
