package com.example.scopeprefixhandler;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/role")
public class HelloRolesApi {

    private String name = "world";

    @GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    @Secured({SecurityConfig.READ_ROLE, SecurityConfig.WRITE_ROLE})
    public String getHello() {
        return "Hello, "+name+"!";
    }

    @PutMapping(path = "/hello", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @Secured(SecurityConfig.WRITE_ROLE)
    public String putHello(@RequestBody String name) {
        this.name = name;
        return getHello();
    }

}
