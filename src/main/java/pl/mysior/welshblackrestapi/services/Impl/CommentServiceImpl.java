package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Comment;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.CommentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final
    CowRepository cowRepository;

    @Autowired
    public CommentServiceImpl(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }

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
            return foundCow;
        } else {
            return null;
        }
    }

    public List<Comment> findAll() {
        List<Cow> allCows = cowRepository.findAll();
        List<Comment> allComments = new ArrayList<>();
        for (Cow c : allCows) {
            if (c.getComments() != null) {
                allComments.addAll(c.getComments());
            }
        }
        allComments.sort(Comment::compareTo);
        return allComments;
    }

    public List<Comment> findLast() {
        List<Comment> allComments = findAll();
        Collections.reverse(allComments);

        List<Comment> lastComments = new ArrayList<>();

        for (Comment com : allComments) {
            if (!containsName(lastComments, com.getCowNumber())) {
                lastComments.add(com);
            }
        }
        return lastComments;
    }

    private boolean containsName(final List<Comment> list, final String number) {
        return list.stream().anyMatch(o -> o.getCowNumber().equals(number));
    }

}