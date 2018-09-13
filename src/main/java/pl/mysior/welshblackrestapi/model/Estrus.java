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
public class Estrus implements Comparable<Estrus> {

    private String cowNumber;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate estrusDate;

    public Estrus(String cowNumber, LocalDate estrusDate) {
        this.cowNumber = cowNumber;
        this.estrusDate = estrusDate;
    }

    public Estrus() {
    }


    @Override
    public int compareTo(Estrus o) {
        return getEstrusDate().compareTo(o.getEstrusDate());
    }
}
