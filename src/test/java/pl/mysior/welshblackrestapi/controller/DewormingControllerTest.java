package pl.mysior.welshblackrestapi.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.mysior.welshblackrestapi.JsonMapper;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.services.DewormingService;

import java.time.LocalDate;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.EXPIRATION_TIME;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.SECRET;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(DewormingController.class)
public class DewormingControllerTest {

    private static final String DEFAULT_URL = "/cows/dewormings";

    private Cow cow1;
    private Cow cow2;
    private Deworming deworming1;
    private Deworming deworming2;
    private Deworming deworming3;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DewormingService dewormingService;

    private String obtainToken() {
        String token = JWT.create().withSubject("username").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return "Bearer " + token;
    }

    @Before
    public void before(){
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        deworming1 = new Deworming("PL123",100,LocalDate.of(2018,1,1));
        deworming1 = new Deworming("PL1234",200,LocalDate.of(2018,2,2));
        deworming1 = new Deworming("PL123",300,LocalDate.of(2019,2,2));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void addDeworming_ShouldReturnBadRequestIfNumberIsNull() throws Exception {
        deworming1.setCowNumber(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(deworming1)))
                .andExpect(status().isBadRequest());
    }




}
