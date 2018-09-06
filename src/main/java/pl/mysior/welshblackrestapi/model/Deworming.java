package pl.mysior.welshblackrestapi.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Calendar;

@Data
@Document
public class Deworming implements Comparable<Deworming> {

    private String cowNumber;
    private int quantity;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dewormingDate;


    public Deworming(String cowNumber, int quantity, LocalDate dewormingDate) {
        this.cowNumber = cowNumber;
        this.quantity = quantity;
        this.dewormingDate = dewormingDate;
    }

    public Deworming() {
    }

    @Override
    public int compareTo(Deworming o) {
        return getDewormingDate().compareTo(o.getDewormingDate());
    }
}
