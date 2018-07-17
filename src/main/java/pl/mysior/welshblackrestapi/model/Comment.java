package pl.mysior.welshblackrestapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;

@Data
@Document
public class Comment {
    @Id
    private String id;
    private String cowNumber;
    private String comment;
    private Calendar commentDate;

    public Comment(String cowNumber, String comment, Calendar commentDate) {
        this.cowNumber = cowNumber;
        this.comment = comment;
        this.commentDate = commentDate;
    }
}
