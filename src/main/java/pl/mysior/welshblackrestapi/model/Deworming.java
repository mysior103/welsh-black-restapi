package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public class Deworming extends CowAction {

    private int quantity;

    public Deworming(String cowNumber, int quantity, LocalDate dewormingDate) {
        super(cowNumber, dewormingDate);
        this.quantity = quantity;
    }

    public Deworming() {
    }
}
