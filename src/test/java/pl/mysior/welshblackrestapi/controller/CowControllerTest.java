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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.EXPIRATION_TIME;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.SECRET;

@RunWith(SpringRunner.class)
@WebMvcTest(CowController.class)

public class CowControllerTest {

    private Cow cow1;
    private Cow cow2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private CowService cowService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private String obtainToken() {
        String token = JWT.create().withSubject("username").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return "Bearer " + token;
    }

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(cowService);
    }

    @Test
    public void addCow_ShouldReturnRepresentationOfCreatedEntity() throws Exception {

        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(post("/cows/")
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(cow1.getNumber()))
                .andExpect(jsonPath("$.name").value(cow1.getName()));
    }

    @Test
    public void getAllCows_ShouldReturnOK() throws Exception {
        Mockito.when(cowService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(cow1, cow2)));
        mockMvc.perform(get("/cows/")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCows_ShouldReturnListOfCows() throws Exception {
        Mockito.when(cowService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(cow1, cow2)));
        mockMvc.perform(get("/cows/")
                .header("Authorization", obtainToken()))
                .andExpect(jsonPath("$[0].number").value(cow1.getNumber()))
                .andExpect(jsonPath("$[1].number").value(cow2.getNumber()));
    }

    @Test
    public void getCow_ShouldReturnRequestedCowByNumber() throws Exception {
        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(cow1);
        mockMvc.perform(get("/cows/" + cow1.getNumber())
                .header("Authorization", obtainToken())
                .param("number", cow1.getNumber()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
    }

    @Test
    public void updateCow_ShouldReturnUpdatedCowIfPresent() throws Exception {
        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(post("/cows/")
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));

        cow1.setName("ChangedName");
        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(put("/cows/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
    }

    @Test
    public void updateCow_ShouldReturnCreateNewCowIfNotExist() throws Exception {
        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(post("/cows/")
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(cow1);
        cow1.setName("ChangedName");
        mockMvc.perform(put("/cows/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
    }

    @Test
    public void updateCow_ShouldBadRequestIfLackOfCowNumber() throws Exception {
        cow1.setNumber("");
        mockMvc.perform(put("/cows/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(status().isBadRequest());
    }


}
