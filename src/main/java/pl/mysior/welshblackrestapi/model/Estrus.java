package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Data
@Document
public class Estrus extends CowAction {

    public Estrus(String cowNumber, LocalDate date) {
        super(cowNumber, date);
    }

    public Estrus() {
    }
}
