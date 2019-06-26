package com.example.scopeprefixhandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import({SecurityConfig.class, OAuth2AutoConfiguration.class, OAuth2ClientAutoConfiguration.class})
public class ScopePrefixHandlerApplicationTests {


    @Autowired
    MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

    @Test
    public void givenNoToken_whenHelloScope_allUnauthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/scope/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.put("/scope/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());

//        mockMvc.perform(MockMvcRequestBuilders.get("/role/hello"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/role/hello")
//                .contentType(MediaType.TEXT_PLAIN).content("Test"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(scopes = {"resource.read"})
    public void givenReadToken_whenHelloScope_thenOnlyAuthorizedAccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/scope/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/scope/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());

//        mockMvc.perform(MockMvcRequestBuilders.get("/role/hello"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk());
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/role/hello")
//                .contentType(MediaType.TEXT_PLAIN).content("Test"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockOAuth2User(scopes = {"goodprefix_resource.read"})
    public void givenReadTokenWithGoodPrefix_whenHelloScope_thenOnlyAuthorizedAccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/scope/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/scope/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockOAuth2User(scopes = {"resource.write"})
    public void givenWriteToken_whenHelloScope_thenAuthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/scope/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/scope/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
                .andExpect(content().string("Hello, Test!"));
    }

    @Test
    @WithMockUser(roles = {"resource.read"})
    public void givenReadToken_whenHelloRole_thenOnlyAuthorizedAccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/role/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/role/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"resource.write"})
    public void givenWriteToken_whenHelloRole_thenAuthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/role/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/role/hello")
                .contentType(MediaType.TEXT_PLAIN).content("Test"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
                .andExpect(content().string("Hello, Test!"));
    }

}