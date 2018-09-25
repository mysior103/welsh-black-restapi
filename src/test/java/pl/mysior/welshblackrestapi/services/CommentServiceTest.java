package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommentServiceTest {

    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private CommentService commentService;

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        comment1 = new Comment("PL123", "This is a comment1!", LocalDate.of(2018, 1, 1));
        comment2 = new Comment("PL1234", "This is a comment2!", LocalDate.of(2017, 3, 4));
        comment3 = new Comment("PL123", "This is a comment3!", LocalDate.of(2019, 3, 4));

    }

    @Test
    public void save_shouldCreateNewListReturnCowWithSavedComment() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(comment1.getCowNumber());
        Cow result = commentService.save(comment1);
        assertEquals(result.getComments().get(0).getComment(), comment1.getComment());
    }

    @Test
    public void save_shouldAddCommentToListReturnCowWithSavedComment() {
        Comment comment3 = new Comment("PL123", "This is a comment3!", LocalDate.of(2019, 3, 4));
        cow1.setComments(new ArrayList<>(Arrays.asList(comment3)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(comment1.getCowNumber());
        Cow result = commentService.save(comment1);
        assertEquals(result.getComments().size(),2);
    }

    @Test
    public void save_shouldReturnNullWhenCowDoesNotExist() {
        doReturn(Optional.empty()).when(cowRepository).findById(comment1.getCowNumber());
        Cow result = commentService.save(comment1);
        assertNull(result);
    }

    @Test
    public void findAll_ShouldReturnListOfAllComments() {
        cow1.setComments(new ArrayList<>(Collections.singletonList(comment1)));
        cow2.setComments(new ArrayList<>(Collections.singletonList(comment2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Comment> result = commentService.findAll();
        assertEquals(result.size(), 2);
    }

    @Test
    public void findAll_ShouldReturnOrderedListOfAllComments() {
        cow1.setComments(new ArrayList<>(Collections.singletonList(comment1)));
        cow2.setComments(new ArrayList<>(Collections.singletonList(comment2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Comment> result = commentService.findAll();
        assertTrue(result.get(0).getCommentDate().isBefore(result.get(1).getCommentDate()));
    }

    @Test
    public void findLast_ShouldReturnLastCommentOfCow() {
        cow1.setComments(new ArrayList<>(Collections.singletonList(comment1)));
        cow2.setComments(new ArrayList<>(Collections.singletonList(comment2)));
        cow1.getComments().add(comment3);

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Comment> result = commentService.findLast();
        assertEquals(result.get(0).getComment(), comment3.getComment());
        assertEquals(result.get(1).getComment(), comment2.getComment());
    }

    @Test
    public void findByCow_shouldReturnOrderedListOfCommentsOfSpecificCow() {
        cow1.setComments(new ArrayList<>(Arrays.asList(comment1,comment3)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        List<Comment> result = commentService.findByCow(cow1.getNumber());
        assertEquals(result.get(0).getCommentDate(), comment1.getCommentDate());
        assertEquals(result.get(1).getCommentDate(), comment3.getCommentDate());
    }
}
