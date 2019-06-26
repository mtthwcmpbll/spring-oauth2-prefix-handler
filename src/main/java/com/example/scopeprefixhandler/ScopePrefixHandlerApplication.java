package com.example.scopeprefixhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@SpringBootApplication
public class ScopePrefixHandlerApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(ScopePrefixHandlerApplication.class, args);
    }

    @RestController
    public class UserInfoApi {
        @GetMapping("/user")
        public Principal user(Principal principal) {
            return principal;
        }
    }

}
