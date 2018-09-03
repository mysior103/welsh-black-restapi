package pl.mysior.welshblackrestapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Calendar;

@Data
@Document
public class Comment implements Comparable<Comment>{

    private String cowNumber;
    private String comment;
    private LocalDate commentDate;

    public Comment(String cowNumber, String comment, LocalDate commentDate) {
        this.cowNumber = cowNumber;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public Comment() {
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public int compareTo(Comment o) {
        return getCommentDate().compareTo(o.getCommentDate());
    }
}
