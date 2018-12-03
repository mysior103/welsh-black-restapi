package pl.mysior.welshblackrestapi.services.DTO;

import lombok.Data;
import pl.mysior.welshblackrestapi.model.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class CowDTO {
    private String number;
    private String name;
    private LocalDate birthDate;
    private String motherNumber;
    private String fatherNumber;
    private String sex;
    private String color;
    private boolean active;
    private List<BloodTest> bloodTests;
    private List<Comment> comments;
    private List<Deworming> dewormings;
    private List<Estrus> estruses;
    private List<Vaccine> vaccines;
}
