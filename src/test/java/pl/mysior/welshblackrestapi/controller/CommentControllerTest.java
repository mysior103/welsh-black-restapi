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
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CommentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private Cow cow1;
    private Cow cow2;
    private Comment comment1;
    private Comment comment2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before(){
        cow1 = new Cow("PL123","imie",LocalDate.of(2015,8,5),"1324","13245","M","Brazowy",true);
        cow2 = new Cow("PL1234","imie2",LocalDate.of(2014,5,4),"1324","13245","M","Brazowy",true);
        comment1 = new Comment("PL123","This is a comment1!",LocalDate.of(2018,4,5));
        comment1 = new Comment("PL1234","This is a comment2!",LocalDate.of(2017,3,4));
    }

    @Test
    public void addComment_ShouldReturnRelatedCowWithComment() throws Exception{
        cow1.setComments(new ArrayList<>(Arrays.asList(comment1)));
        Mockito.when(commentService.save(comment1)).thenReturn(cow1);
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(cow1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(cow1.getNumber()))
                .andExpect(jsonPath("$.name").value(cow1.getName()));
    }


}
