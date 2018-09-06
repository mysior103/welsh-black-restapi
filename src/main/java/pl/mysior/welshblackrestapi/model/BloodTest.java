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
public class BloodTest implements Comparable<BloodTest>{
    private String cowNumber;
    private boolean result;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testDate;

    public BloodTest(String cowNumber, boolean result, LocalDate testDate) {
        this.cowNumber = cowNumber;
        this.result = result;
        this.testDate = testDate;
    }

    public BloodTest() {
    }


    @Override
    public int compareTo(BloodTest o) {
        return getTestDate().compareTo(o.getTestDate());
    }
}
