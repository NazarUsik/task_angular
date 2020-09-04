import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {UserListComponent} from './user-list/user-list.component';
import {UserFormComponent} from './user-form/user-form.component';
import {UserService} from './service/user.service';
import {KeycloakService} from './service/keycloak.service';
import {LoginFormComponent} from './login-form/login-form.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TokenInterceptor} from './service/token-interceptor';

export function kcFactory(keycloakService: KeycloakService) {
    return () => keycloakService.init();
}

export function irFactory(keycloak: KeycloakService) {
    return new TokenInterceptor(keycloak);
}

@NgModule({
    declarations: [
        AppComponent,
        UserListComponent,
        UserFormComponent,
        LoginFormComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        BrowserAnimationsModule
    ],
    providers: [
        UserService,
        KeycloakService,
        {
            provide: APP_INITIALIZER,
            useFactory: kcFactory,
            deps: [KeycloakService],
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useFactory: irFactory,
            deps: [KeycloakService],
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
