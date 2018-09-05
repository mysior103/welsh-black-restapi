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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.BloodTestService;
import pl.mysior.welshblackrestapi.services.CowService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private CowService cowService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before(){
        cow1 = new Cow("PL123","imie",LocalDate.of(2015,8,5),"1324","13245","M","Brazowy",true);
        cow2 = new Cow("PL1234","imie2",LocalDate.of(2014,5,4),"1324","13245","M","Brazowy",true);
        bloodTest1 = new BloodTest("PL123",true,LocalDate.of(2018,1,1));
        bloodTest2 = new BloodTest("PL1234",false,LocalDate.of(2018,2,2));
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
    public void save_ReturnBadRequestIfLackOfCowNumber() throws Exception {
        BloodTest bloodTest = new BloodTest("",true,LocalDate.of(2011,1,1));
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_ReturnNotFoundIfCowDoesNotExist() throws Exception {
        Mockito.when(bloodTestService.save(any(BloodTest.class))).thenReturn(null);
        mockMvc.perform(post("/bloodtests")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(bloodTest1)))
                .andExpect(status().isNotFound());
    }



}
