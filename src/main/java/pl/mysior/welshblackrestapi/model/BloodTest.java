package pl.mysior.welshblackrestapi.model;



import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public class BloodTest extends CowAction {
    private boolean result;
    public BloodTest(String cowNumber, boolean result, LocalDate testDate) {
        super(cowNumber,testDate);
        this.result = result;
    }
    public BloodTest() {
    }
}
