package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CommentService;
import pl.mysior.welshblackrestapi.services.CowService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class CommentController {

    private static final String OPERATION = "Comment";

    @Autowired
    CowService cowService;
    @Autowired
    CommentService commentService;

    @PostMapping(path = "/comments")
    public ResponseEntity<Cow> addComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        if (comment.getCowNumber() == "" || comment.getCowNumber() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = commentService.save(comment);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/comments"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }


    @GetMapping(path = "/comments")
    public List<Comment> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{number}/comments")
    public List<Comment> getComments(@PathVariable String number) {
        Cow foundCow = cowService.findByNumber(number);
        if (foundCow != null) {
            return foundCow.getComments();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping(path = "/comments/last")
    public List<Comment> getLastComments() {
        return commentService.findLast();
    }


}