package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;

import java.util.List;

public interface CommentService {
    Cow save(Comment comment);

    List<Comment> findAll();

    List<Comment> findLast();

    List<Comment> findByCow(String cowNumber);

}

