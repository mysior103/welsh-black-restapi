package pl.mysior.welshblackrestapi.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document
public class Vaccine extends CowAction {

    public Vaccine(String cowNumber, LocalDate vaccineDate) {
        super(cowNumber, vaccineDate);
    }

    public Vaccine() {
    }


}