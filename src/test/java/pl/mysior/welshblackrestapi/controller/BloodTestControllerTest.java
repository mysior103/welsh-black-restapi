package pl.mysior.welshblackrestapi.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.security.JWTAuthenticationFilter;
import pl.mysior.welshblackrestapi.security.user.ApplicationUserRepository;
import pl.mysior.welshblackrestapi.security.user.UserController;
import pl.mysior.welshblackrestapi.services.BloodTestService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.EXPIRATION_TIME;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.SECRET;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(BloodTestController.class)
public class BloodTestControllerTest {

    private Cow cow1;
    private Cow cow2;
    private BloodTest bloodTest1;
    private BloodTest bloodTest2;
    private BloodTest bloodTest3;

    private static final String DEFAULT_URL = "/cows/bloodtests";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private BloodTestService bloodTestService;

    @MockBean
    ApplicationUserRepository applicationUserRepository;


    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1324");
        bloodTest1 = new BloodTest("PL123", true, LocalDate.of(2018, 1, 1));
        bloodTest2 = new BloodTest("PL1234", false, LocalDate.of(2018, 2, 2));
        bloodTest3 = new BloodTest("PL123", false, LocalDate.of(2019, 2, 2));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();

    }

    private String obtainToken() {
        String token = JWT.create().withSubject("username").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return "Bearer "+token;
    }

    @Test
    public void save_ShouldReturnBadRequestIfNumberIsNull() throws Exception {
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization",obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_ShouldReturnBadRequestIfLackOfCowNumber() throws Exception {
        mockMvc.perform(post(DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_ShouldReturnNotFoundIfCowDoesNotExist() throws Exception {
        when(bloodTestService.save(bloodTest1)).thenReturn(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization",obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void save_ShouldReturnIsCreated() throws Exception {
        cow1.setBloodTests(new ArrayList<>(Arrays.asList(bloodTest1)));
        when(bloodTestService.save(bloodTest1)).thenReturn(cow1);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization",obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllBloodTests_ShouldReturnEmptyListIfLackOfBloodTests() throws Exception {
        when(bloodTestService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization",obtainToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllBloodTest_ShouldReturnAllBloodTestsFromAllCows() throws Exception {
        when(bloodTestService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(bloodTest1,bloodTest2)));
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization",obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(bloodTest1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(bloodTest2.getCowNumber()));
    }
    @Test
    public void getBloodTests_ShouldReturnAllBloodTestFromSpecificCow() throws Exception{
       when(bloodTestService.findByCow(any(String.class))).thenReturn(new ArrayList<>(Arrays.asList(bloodTest1,bloodTest3)));
        mockMvc.perform(get("/cows/" + cow1.getNumber() + "/bloodtests")
                .header("Authorization",obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(bloodTest1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(bloodTest3.getCowNumber()));
    }
    @Test
    public void getLastBloodTests_ShouldReturnOrderedBloodTestsOfAllCows() throws Exception {
        cow1.setBloodTests(new ArrayList<>(Arrays.asList(bloodTest1,bloodTest3)));
        cow2.setBloodTests(new ArrayList<>(Arrays.asList(bloodTest2)));
        when(bloodTestService.findLast()).thenReturn(new ArrayList<>(Arrays.asList(bloodTest3, bloodTest2)));
        mockMvc.perform(get(DEFAULT_URL + "/last")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(bloodTest3.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(bloodTest2.getCowNumber()));
    }
}
