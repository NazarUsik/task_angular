package com.nixsolutions.usik.config;

import com.nixsolutions.usik.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling()

                .and()
                .authorizeRequests()
                .antMatchers("/login", "/registration")
                .anonymous()

                .antMatchers("/accessDenied")
                .permitAll()

                .antMatchers("/", "home")
                .fullyAuthenticated()

                .antMatchers("/addUser", "/editUser", "/deleteUser")
                .access("hasRole('ADMIN')")

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .accessDeniedPage("/accessDenied");

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();

        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);


    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            setCorsInHeader(response);
            response.getWriter().append("OK");
            response.setStatus(200);
        };
    }

    private AuthenticationFailureHandler failureHandler() {
        return (request, response, e) -> {
            setCorsInHeader(response);
            response.getWriter().append("Authentication failure");
            response.setStatus(401);
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            setCorsInHeader(response);
            response.getWriter().append("Access denied");
            response.setStatus(403);
        };
    }

    private void setCorsInHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}