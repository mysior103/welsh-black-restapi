package pl.mysior.welshblackrestapi.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document
public class Estrus extends CowAction {

    public Estrus(String cowNumber, LocalDate date) {
        super(cowNumber, date);
    }

    public Estrus() {
    }
}
