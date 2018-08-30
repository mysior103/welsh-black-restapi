package pl.mysior.welshblackrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;

import java.util.List;

public interface CommentService {

    Cow save(Comment comment);

    List<Comment> findAll();

}

