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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Cow.class)
public class CowControllerTest {

    private Cow cow;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CowService cowService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before(){
        cow = new Cow("PL123","imie","1324","13245","M","Brazowy",true);
    }

    @Test
    public void createCow_ShouldReturnRepresentationOfCreatedEntity() throws Exception {

        Mockito.when(cowService.save(cow)).thenReturn(cow);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/cows/")
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .content(mapToJson(cow))
//                .accept(MediaType.APPLICATION_JSON_UTF8);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse respone = result.getResponse();
//        Assert.assertEquals(HttpStatus.CREATED.value(),respone.getStatus());
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow)))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.number").value(cow.getNumber()));
    }
    @Test
    public void getAllCows_ShouldReturnListOfAllCows() throws Exception{
        Mockito.when(cowService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(cow)));
        System.out.println("****************" + cowService.findAll());
        MvcResult results = mockMvc.perform(get("/cows/")).andReturn();
        System.out.println("****************" + results.getResponse().getContentAsString());
        MockHttpServletResponse response = results.getResponse();
        Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }


}
