package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Calendar;
@Data
@Document
public class BloodTest implements Comparable<BloodTest>{
    private LocalDate testDate;
    private String cowNumber;
    private boolean result;

    public BloodTest(LocalDate testDate, String cowNumber, boolean result) {
        this.testDate = testDate;
        this.cowNumber = cowNumber;
        this.result = result;
    }

    public BloodTest() {
    }


    @Override
    public int compareTo(BloodTest o) {
        return getTestDate().compareTo(o.getTestDate());
    }
}
