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
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.model.Vaccine;
import pl.mysior.welshblackrestapi.services.VaccineService;

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
@WebMvcTest(VaccineController.class)
public class VaccineControllerTest {

    private static final String DEFAULT_URL = "/cows/vaccines";

    private Cow cow1;
    private Cow cow2;
    private Vaccine vaccine1;
    private Vaccine vaccine2;
    private Vaccine vaccine3;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private VaccineService vaccineService;

    private String obtainToken() {
        String token = JWT.create().withSubject("username").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return "Bearer " + token;
    }

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        vaccine1 = new Vaccine("PL123", LocalDate.of(2018, 1, 1));
        vaccine2 = new Vaccine("PL1234", LocalDate.of(2018, 2, 2));
        vaccine3 = new Vaccine("PL123", LocalDate.of(2019, 2, 2));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void addDeworming_ShouldReturnBadRequestIfNumberIsNull() throws Exception {
        vaccine1.setCowNumber(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(vaccine1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addVaccine_ShouldReturnBadRequestIfNumberIsEmpty() throws Exception {
        vaccine1.setCowNumber("");
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(vaccine1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addVaccine_ShouldReturnNotFoundIfCowDoesNotExist() throws Exception {
        when(vaccineService.save(vaccine1)).thenReturn(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(vaccine1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addVaccine_ShouldReturnRelatedCowWithVaccine() throws Exception {
        cow1.setVaccines(new ArrayList<>(Arrays.asList(vaccine1)));
        when(vaccineService.save(vaccine1)).thenReturn(cow1);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.mapToJson(vaccine1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vaccines[0].cowNumber").value(cow1.getVaccines().get(0).getCowNumber()));
    }

    @Test
    public void getAllVaccines_ShouldReturnEmptyListIfLackOfVaccines() throws Exception {
        when(vaccineService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllVaccines_ShouldReturnListOfAllComments() throws Exception {
        cow1.setVaccines(new ArrayList<>(Arrays.asList(vaccine1)));
        cow2.setVaccines(new ArrayList<>(Arrays.asList(vaccine2)));
        when(vaccineService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(vaccine1,vaccine2)));
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(vaccine1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(vaccine2.getCowNumber()));
    }

    @Test
    public void getVaccines_ShouldReturnAllVaccinesFromSpecificCow() throws Exception {
        when(vaccineService.findByCow(any(String.class))).thenReturn(new ArrayList<>(Arrays.asList(vaccine1,vaccine3)));
        mockMvc.perform(get("/cows/" + cow1.getNumber() + "/vaccines")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(vaccine1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(vaccine3.getCowNumber()));
    }

    @Test
    public void getLastVaccines_ShouldReturnOrderedVaccinesOfAllCows() throws Exception {
        cow1.setVaccines(new ArrayList<>(Arrays.asList(vaccine1,vaccine3)));
        cow2.setVaccines(new ArrayList<>(Arrays.asList(vaccine2)));
        when(vaccineService.findLast()).thenReturn(new ArrayList<>(Arrays.asList(vaccine3,vaccine2)));
        mockMvc.perform(get(DEFAULT_URL + "/last")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(vaccine3.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(vaccine2.getCowNumber()));
    }

}
