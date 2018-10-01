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
import java.util.List;

@Data
@Document
public class Cow {
    @Id
    private String number;
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    public Cow(String number, String name, LocalDate birthDate, String motherNumber, String fatherNumber, String sex, String color, boolean active) {

        this.name = name;
        this.number = number;
        this.birthDate = birthDate;
        this.motherNumber = motherNumber;
        this.fatherNumber = fatherNumber;
        this.sex = sex;
        this.color = color;
        this.active = active;
    }

    public Cow() {
    }
}
