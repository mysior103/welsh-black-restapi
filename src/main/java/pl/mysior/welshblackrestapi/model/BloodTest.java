package pl.mysior.welshblackrestapi.model;


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
