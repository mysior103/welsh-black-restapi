package pl.mysior.welshblackrestapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
@Data
@Document
public class Estrus {
    private Calendar estrusDate;

    public Estrus(Calendar estrusDate) {
        this.estrusDate = estrusDate;
    }
}
