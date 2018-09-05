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
import pl.mysior.welshblackrestapi.services.CowService;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private CowService cowService;

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Before
    public void before() throws URISyntaxException {
        cow1 = new Cow("PL123","imie",LocalDate.of(2015,8,5),"1324","13245","M","Brazowy",true);
        cow2 = new Cow("PL1234","imie2",LocalDate.of(2014,5,4),"1324","13245","M","Brazowy",true);
        comment1 = new Comment("PL123","This is a comment1!",LocalDate.of(2018,4,5));
        comment2 = new Comment("PL1234","This is a comment2!",LocalDate.of(2017,3,4));
        cow1.setComments(new ArrayList<>(Arrays.asList(comment1)));
        cow2.setComments(new ArrayList<>(Arrays.asList(comment2)));
    }

    @Test
    public void addComment_ShouldReturnRelatedCowWithComment() throws Exception{
        Mockito.when(commentService.save(comment1)).thenReturn(cow1);
        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(comment1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments[0].comment").value(cow1.getComments().get(0).getComment()));
    }

    @Test
    public void getAllComments_ShouldReturnListOfAllComments() throws Exception{
        Mockito.when(commentService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(comment1,comment2)));
        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value(comment1.getComment()))
                .andExpect(jsonPath("$[1].comment").value(comment2.getComment()));
    }
    @Test
    public void getComment_ShouldReturnOrderedListOfCommentsOfSpecificCow() throws Exception{
        Comment comment3 = new Comment("PL123","This is a comment3!",LocalDate.of(2018,12,2));
        cow1.getComments().add(comment3);
        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(cow1);
        mockMvc.perform(get("/"+ cow1.getNumber()+"/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value(comment1.getComment()))
                .andExpect(jsonPath("$[1].comment").value(comment3.getComment()));
    }

    @Test
    public void getLastComment_ShouldReturnOrderedLastCommentOfAllCows()throws Exception{
        Comment comment3 = new Comment("PL123","This is a comment3!",LocalDate.of(2018,12,2));
        cow1.getComments().add(comment3);
        Mockito.when(commentService.findLast()).thenReturn(new ArrayList<>(Arrays.asList(comment3,comment2)));
        mockMvc.perform(get("/comments/last"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value(comment3.getComment()))
                .andExpect(jsonPath("$[1].comment").value(comment2.getComment()));
    }


}
