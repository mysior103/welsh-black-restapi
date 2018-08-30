package pl.mysior.welshblackrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CommentRepository;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CowRepository cowRepository;
    @Autowired
    CommentRepository commentRepository;


    public Cow save(Comment comment) {
        Cow foundCow;
        Optional<Cow> optCow = cowRepository.findById(comment.getCowNumber());
        if (optCow.isPresent()) {
            foundCow = optCow.get();
            List<Comment> commentList = foundCow.getComments();
            if (commentList != null) {
                commentList.add(comment);
            } else {
                commentList = new ArrayList<>();
                commentList.add(comment);
            }
            foundCow.setComments(commentList);
            cowRepository.save(foundCow);
        } else {
            foundCow = new Cow();// troche mi sie to nie podoba, ale nie mam pomyslu jak to zrobic
        }
        return foundCow;
    }

    public List<Comment> findAll() {
        List<Cow> allCows = cowRepository.findAll();
        List<Comment> allComments = new ArrayList<>();
        for (Cow c : allCows) {
            if (c.getComments() != null) {
                allComments.addAll(c.getComments());
            }
        }
        return allComments;
    }

}
