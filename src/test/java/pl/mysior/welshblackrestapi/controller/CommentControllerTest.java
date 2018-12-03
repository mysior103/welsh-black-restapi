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
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowActionService;
import pl.mysior.welshblackrestapi.services.CowService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.EXPIRATION_TIME;
import static pl.mysior.welshblackrestapi.security.SecurityConstants.SECRET;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private static final String DEFAULT_URL = "/cows/comments";

    private Cow cow1;
    private Cow cow2;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private CowActionService<Comment> commentService;

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
        comment1 = new Comment("PL123", "This is a comment1!", LocalDate.of(2018, 1, 1));
        comment2 = new Comment("PL1234", "This is a comment2!", LocalDate.of(2018, 2, 2));
        comment3 = new Comment("PL123", "This is a comment3!", LocalDate.of(2019, 2, 2));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void addComment_ShouldReturnBadRequestIfNumberIsNull() throws Exception {
        comment1.setCowNumber(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(comment1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addComment_ShouldReturnBadRequestIfLackOfCowNumber() throws Exception {
        comment1.setCowNumber("");
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(comment1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addComment_ShouldReturnNotFoundIfCowDoesNotExist() throws Exception {
        when(commentService.save(comment1)).thenReturn(null);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(comment1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addComment_ShouldReturnIsCreatedAndRelatedCowWithComment() throws Exception {
        cow1.setComments(new ArrayList<>(Arrays.asList(comment1,comment2)));
        when(commentService.save(comment1)).thenReturn(cow1);
        mockMvc.perform(post(DEFAULT_URL)
                .header("Authorization", obtainToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapToJson(comment1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments[0].comment").value(cow1.getComments().get(0).getComment()));
    }

    @Test
    public void getAllComments_ShouldReturnEmptyListIfLackOfComments() throws Exception {
        when(commentService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllComments_ShouldReturnListOfAllComments() throws Exception {
        Mockito.when(commentService.findAll()).thenReturn(new ArrayList<>(Arrays.asList(comment1, comment2)));
        mockMvc.perform(get(DEFAULT_URL)
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value(comment1.getComment()))
                .andExpect(jsonPath("$[1].comment").value(comment2.getComment()));
    }

    @Test
    public void getComment_ShouldReturnOrderedListOfCommentsOfSpecificCow() throws Exception {
//        cow1.setComments(new ArrayList<>(Arrays.asList(comment1)));
//        cow1.getComments().add(comment3);
//
//        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(CowMapper.toDto(cow1));
//        mockMvc.perform(get("/cows/" + cow1.getNumber() + "/comments")
//                .header("Authorization", obtainToken()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].comment").value(comment1.getComment()))
//                .andExpect(jsonPath("$[1].comment").value(comment3.getComment()));
    }

    @Test
    public void getComment_ShouldReturnEmptyListForSpecificCow() throws Exception {
//        cow1.setComments(new ArrayList<>());
//        Mockito.when(cowService.findByNumber(cow1.getNumber())).thenReturn(new CowDTO());
//        mockMvc.perform(get("/cows/" + cow1.getNumber() + "/comments")
//                .header("Authorization", obtainToken()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("[]"));
    }

    @Test
    public void getLastComment_ShouldReturnOrderedLastCommentOfAllCows() throws Exception {
        Comment comment3 = new Comment("PL123", "This is a comment3!", LocalDate.of(2018, 12, 2));
        cow1.setComments(new ArrayList<>(Arrays.asList(comment1,comment3)));
        cow2.setComments(new ArrayList<>(Arrays.asList(comment2)));
        Mockito.when(commentService.findLast()).thenReturn(new ArrayList<>(Arrays.asList(comment3, comment2)));
        mockMvc.perform(get(DEFAULT_URL + "/last")
                .header("Authorization", obtainToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value(comment3.getComment()))
                .andExpect(jsonPath("$[1].comment").value(comment2.getComment()));
    }
}
