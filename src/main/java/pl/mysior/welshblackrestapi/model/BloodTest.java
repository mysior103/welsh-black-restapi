package pl.mysior.welshblackrestapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
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
