package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
@Data
@Document
public class Vaccine {
    @Id
    private String id;
    private Calendar vaccineDate;

    public Vaccine(Calendar vaccineDate) {
        this.vaccineDate = vaccineDate;
    }
}