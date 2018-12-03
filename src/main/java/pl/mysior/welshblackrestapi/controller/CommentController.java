package pl.mysior.welshblackrestapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.exception.CowNotFoundException;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowActionService;
import pl.mysior.welshblackrestapi.services.CowService;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    private static final String OPERATION = "Comment";

    @Autowired
    CowService cowService;
    @Autowired
    private CowActionService<Comment> commentService;

    @PostMapping(path = "/comments")
    public ResponseEntity<Cow> addComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        if (comment.getCowNumber() == "" || comment.getCowNumber() == null) {
            logger.error("POST Lack of cow number");
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = commentService.save(comment);
            if (saved == null) {
                logger.warn("POST Cow with number " + comment.getCowNumber() + " couldn't find");
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                logger.info("POST Comment for cow " + comment.getCowNumber() + " has been added");
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/comments"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }


    @GetMapping(path = "/comments")
    public List<Comment> getAllComments() {
        logger.info("GET List of all blood tests has been generated");
        return commentService.findAll();
    }

    @GetMapping("/{number}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String number) {
        try {
            CowDTO foundCow = cowService.findByNumber(number);
            return ResponseEntity.status(HttpStatus.OK).body(foundCow.getComments());
        } catch (CowNotFoundException e) {
            logger.info("Could not find cow with number: ", number);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/comments/last")
    public List<Comment> getLastComments() {
        return commentService.findLast();
    }


}