package kr.co.hanbit.mastering.springmvc4.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import kr.co.hanbit.mastering.MasteringSpringMvc4Application;
import kr.co.hanbit.mastering.springmvc4.SessionBuilder;
import kr.co.hanbit.mastering.springmvc4.profile.UserProfileSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Jeon on 2017-04-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class HomeControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void test_should_redirect_to_profile() throws  Exception {

        this.mockMvc.perform(get("/"))
                .andDo(print()) // 디버그 출력물을 생성
                .andExpect((status().isFound()))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void test_should_redirect_to_tastes() throws Exception {

        MockHttpSession session = new SessionBuilder().userTastes("spring","groovy").build();
        //MockHttpSession session = new MockHttpSession();

        /* SessionBuilder 내부에서 처리함.
        UserProfileSession sessionBean = new UserProfileSession();
        sessionBean.toForm().setTastes(Arrays.asList("spring","groovy"));
        session.setAttribute("scopedTarget.userProfileSession", sessionBean);*/
        this.mockMvc.perform(get("/").session(session))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/search/mixed;keywords=spring,groovy"));

    }
    @After
    public void tearDown() throws Exception {

    }

}

