package pl.mysior.welshblackrestapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CowController.class)

public class CowControllerTest {

    private Cow cow1;
    private Cow cow2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CowService cowService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before() {
        cow1 = new Cow("PL123", "imie", LocalDate.of(2015, 8, 5), "1324", "13245", "M", "Brazowy", true);
        cow2 = new Cow("PL1234", "imie2", LocalDate.of(2014, 5, 4), "1324", "13245", "M", "Brazowy", true);
    }

    @Test
    public void contextLoads() {
        assertNotNull(cowService);
    }

    @Test
    public void createCow_ShouldReturnRepresentationOfCreatedEntity() throws Exception {

        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(cow1.getNumber()))
                .andExpect(jsonPath("$.name").value(cow1.getName()));
    }

    @Test
    public void getAllCows_ShouldReturnOK() throws Exception {
        Mockito.when(cowService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(cow1, cow2)));
        MvcResult results = mockMvc.perform(get("/")).andReturn();
        MockHttpServletResponse response = results.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getAllCows_ShouldReturnListOfCows() throws Exception {
        Mockito.when(cowService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(cow1, cow2)));
        mockMvc.perform(get("/"))
                .andExpect(jsonPath("$[0].number").value(cow1.getNumber()))
                .andExpect(jsonPath("$[1].number").value(cow2.getNumber()));
    }

    @Test
    public void getCow_ShouldReturnRequestedCowByNumber() throws Exception {
        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(cow1);
        mockMvc.perform(get("/" + cow1.getNumber()).param("number", cow1.getNumber()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
    }

    @Test
    public void updateCow_ShouldReturnUpdatedCowIfPresent() throws Exception {
        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));

        cow1.setName("ChangedName");
        Mockito.when(cowService.save(cow1)).thenReturn(cow1);
        mockMvc.perform(put("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(jsonPath("$.name").value(cow1.getName()))
                .andExpect(jsonPath("$.number").value(cow1.getNumber()));
    }

}
