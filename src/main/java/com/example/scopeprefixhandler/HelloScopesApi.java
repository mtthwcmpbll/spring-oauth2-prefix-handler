package com.example.scopeprefixhandler;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/scope")
public class HelloScopesApi {

    private String name = "world";

    @GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("#oauth2.hasAnyScope('resource.read', 'resource.write')")
    public String getHello() {
        return "Hello, "+name+"!";
    }

    @PutMapping(path = "/hello", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("#oauth2.hasScope('resource.write')")
    public String putHello(@RequestBody String name) {
        this.name = name;
        return getHello();
    }

}
