package pl.mysior.welshblackrestapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document
public class Comment extends CowAction {

    private String comment;

    public Comment(String cowNumber, String comment, LocalDate commentDate) {
        super(cowNumber, commentDate);
        this.comment = comment;
    }

    public Comment() {
    }
}
