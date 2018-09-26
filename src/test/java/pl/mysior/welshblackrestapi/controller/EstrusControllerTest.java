package pl.mysior.welshblackrestapi.controller;

import com.auth0.jwt.JWT;
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
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.services.EstrusService;

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
@WebMvcTest(EstrusController.class)
public class EstrusControllerTest {
    private static final String DEFAULT_URL = "/cows/estruses";

    private Cow cow1;
    private Cow cow2;
    private Estrus estrus1;
    private Estrus estrus2;
    private Estrus estrus3;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EstrusService estrusService;

    private String obtainToken() {
        String token = JWT.create().withSubject("username").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return "Bearer " + token;
    }

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        estrus1 = new Estrus("PL123", LocalDate.of(2018, 1, 1));
        estrus2 = new Estrus("PL1234",  LocalDate.of(2018, 2, 2));
        estrus3 = new Estrus("PL123", LocalDate.of(2019, 2, 2));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void addEstrus_ShouldReturnBadRequestIfNumberIsNull() throws Exception {
        estrus1.setCowNumber(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(estrus1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addDEstrus_ShouldReturnBadRequestIfNumberIsEmpty() throws Exception {
        estrus1.setCowNumber("");
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(estrus1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addEstrus_ShouldReturnNotFoundIfCowDoesNotExist() throws Exception {
        when(estrusService.save(estrus1)).thenReturn(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(estrus1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addEstrus_ShouldReturnRelatedCowWithEstruses() throws Exception {
        cow1.setEstruses(new ArrayList<>(Arrays.asList(estrus1)));
        when(estrusService.save(estrus1)).thenReturn(cow1);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(estrus1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estruses[0].cowNumber").value(cow1.getNumber()));
    }

    @Test
    public void getAllEstruses_ShouldReturnEmptyListIfLackOfEstruses() throws Exception {
        when(estrusService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllEstruses_ShouldReturnListOfAllEstruses() throws Exception {
        cow1.setEstruses(new ArrayList<>(Arrays.asList(estrus1)));
        cow2.setEstruses(new ArrayList<>(Arrays.asList(estrus2)));
        when(estrusService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(estrus1,estrus2)));
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(estrus1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(estrus2.getCowNumber()));
    }

    @Test
    public void getEstruses_ShouldReturnAllEstrusesFromSpecificCow() throws Exception {
        when(estrusService.findByCow(any(String.class))).thenReturn(new ArrayList<>(Arrays.asList(estrus1,estrus3)));
        mockMvc.perform(get("/cows/" + cow1.getNumber() + "/estruses")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(estrus1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(estrus3.getCowNumber()));
    }

    @Test
    public void getLastEstruses_ShouldReturnOrderedEstrusesOfAllCows() throws Exception {
        cow1.setEstruses(new ArrayList<>(Arrays.asList(estrus1,estrus3)));
        cow2.setEstruses(new ArrayList<>(Arrays.asList(estrus2)));
        when(estrusService.findLast()).thenReturn(new ArrayList<>(Arrays.asList(estrus3,estrus2)));
        mockMvc.perform(get(DEFAULT_URL + "/last")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(estrus3.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(estrus2.getCowNumber()));
    }
}
