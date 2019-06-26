package com.example.scopeprefixhandler;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuth2ScopeSecurityContextFactory.class)
public @interface WithMockOAuth2User {

    String[] scopes() default {};
    String[] roles() default {};

}
