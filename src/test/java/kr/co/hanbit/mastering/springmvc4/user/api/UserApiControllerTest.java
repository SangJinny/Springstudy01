package kr.co.hanbit.mastering.springmvc4.user.api;

import kr.co.hanbit.mastering.springmvc4.user.User;
import kr.co.hanbit.mastering.springmvc4.user.UserRepository;
import java.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Jeon on 2017-04-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class UserApiControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilter;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilter).build();
        userRepository.reset(new User("bob@spring.io"));
    }
    @Test
    public void should_list_users() throws Exception {

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", basicAuth("admin", "admin")))


                .andExpect(status().isOk());/*
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("bob@spring.io")));*/
    }

    private String basicAuth(String login, String password) {
        byte[] auth = (login + ":"+password).getBytes();
        return "Basic "+Base64.getEncoder().encodeToString(auth);
    }
}