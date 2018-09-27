package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Data
@Document
public class Vaccine extends CowAction {

    public Vaccine(String cowNumber, LocalDate vaccineDate) {
        super(cowNumber, vaccineDate);
    }

    public Vaccine() {
    }


}