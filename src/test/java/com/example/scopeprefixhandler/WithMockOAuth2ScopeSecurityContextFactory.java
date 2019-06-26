package com.example.scopeprefixhandler;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.io.Serializable;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WithMockOAuth2ScopeSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2User> {

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2User mockAuthentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = getOAuth2Authentication(mockAuthentication.scopes(), mockAuthentication.roles());
        context.setAuthentication(auth);
        return context;
    }

    private Authentication getOAuth2Authentication(String[] scopes, String[] roles) {
        return new OAuth2Authentication(getOauth2Request(scopes), getAuthentication(roles));
    }

    private OAuth2Request getOauth2Request (String[] oauth2Scopes) {
        String clientId = "oauth-client-id";
        Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = "http://my-redirect-url.com";
        Set<String> responseTypes = Collections.emptySet();
        Set<String> scopes = new HashSet<>();
        scopes.addAll(Arrays.asList(oauth2Scopes));
        Set<String> resourceIds = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        List<GrantedAuthority> authorities = Collections.emptyList(); //AuthorityUtils.createAuthorityList("");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities,
                approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);

        return oAuth2Request;
    }

    private Authentication getAuthentication(String[] userRoles) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);

        User userPrincipal = new User("user", "", true, true, true, true, authorities);

        HashMap<String, String> details = new HashMap<String, String>();
        details.put("user_name", "testuser");
        details.put("email", "test@user.org");
        details.put("name", "Test User");

        TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);
        token.setDetails(details);

        return token;
    }

    private OAuth2ClientContext getOauth2ClientContext () {
        OAuth2ClientContext mockClient = mock(OAuth2ClientContext.class);
        when(mockClient.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("my-fun-token"));

        return mockClient;
    }
}
