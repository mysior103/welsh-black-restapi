package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
@Data
@Document
public class Deworming {
    @Id
    private String id;
    private Calendar dewormingDate;
    private int quantity;

    public Deworming(Calendar dewormingDate, int quantity) {
        this.dewormingDate = dewormingDate;
        this.quantity = quantity;
    }
}
