package pl.mysior.welshblackrestapi.controller;

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
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.BloodTestService;
import pl.mysior.welshblackrestapi.services.CowService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BloodTestController.class)
public class BloodTestControllerTest {

    private Cow cow1;
    private Cow cow2;
    private BloodTest bloodTest1;
    private BloodTest bloodTest2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BloodTestService bloodTestService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before() {
        cow1 = new Cow("PL123", "imie", LocalDate.of(2015, 8, 5), "1324", "13245", "M", "Brazowy", true);
        cow2 = new Cow("PL1234", "imie2", LocalDate.of(2014, 5, 4), "1324", "13245", "M", "Brazowy", true);
        bloodTest1 = new BloodTest("PL123", true, LocalDate.of(2018, 1, 1));
        bloodTest2 = new BloodTest("PL1234", false, LocalDate.of(2018, 2, 2));
    }

    @Test
    public void save_ReturnBadRequestIfLackOfData() throws Exception {
        BloodTest bloodTest = null;
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_ShouldReturnBadRequestIfLackOfCowNumber() throws Exception {
        BloodTest bloodTest = new BloodTest("", true, LocalDate.of(2011, 1, 1));
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_ShouldReturnNotFoundIfCowDoesNotExist() throws Exception {
        when(bloodTestService.save(bloodTest1)).thenReturn(null);
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void save_ShouldReturnIsCreated() throws Exception {
        cow1.setBloodTests(new ArrayList<>(Arrays.asList(bloodTest1)));
        when(bloodTestService.save(bloodTest1)).thenReturn(cow1);
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllBloodTests_ShouldReturnEmptyListIfLackOfBloodTests() throws Exception {
        when(bloodTestService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/bloodtests"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllBloodTest_ShouldReturnAllBloodTestsFromAllCows() throws Exception {
        when(bloodTestService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(bloodTest1,bloodTest2)));
        mockMvc.perform(get("/bloodtests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(bloodTest1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(bloodTest2.getCowNumber()));
    }
    @Test
    public void getBloodTest_ShouldReturnAllBloodTestFromSpecificCow() throws Exception{
        BloodTest bloodTest3 = new BloodTest("PL123", false, LocalDate.of(2019, 2, 2));
        when(bloodTestService.findByCow(any(String.class))).thenReturn(new ArrayList<>(Arrays.asList(bloodTest1,bloodTest3)));
        mockMvc.perform(get("/" + cow1.getNumber() + "/bloodtests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowNumber").value(bloodTest1.getCowNumber()))
                .andExpect(jsonPath("$[1].cowNumber").value(bloodTest3.getCowNumber()));
    }


}
