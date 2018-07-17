package pl.mysior.welshblackrestapi.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
@Data
@Document
public class BloodTest {

    public BloodTest(Calendar testDate, String cowNumber, boolean result) {
        this.testDate = testDate;
        this.cowNumber = cowNumber;
        this.result = result;
    }

    @Id
    private String id;

    private Calendar testDate;
    private String cowNumber;
    private boolean result;

}
