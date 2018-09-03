package pl.mysior.welshblackrestapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
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
    private LocalDate birth_date;
    private String mother_number;
    private String father_number;
    private String sex;
    private String color;
    private boolean active;
    private List<BloodTest> bloodTests;
    private List<Comment> comments;
    private List<Deworming> dewormings;
    private List<Estrus> estruses;
    private List<Vaccine> vaccines;

    public Cow(String number, String name, LocalDate birth_date, String mother_number, String father_number, String sex, String color, boolean active) {

        this.name = name;
        this.number = number;
        this.birth_date = birth_date;
        this.mother_number = mother_number;
        this.father_number = father_number;
        this.sex = sex;
        this.color = color;
        this.active = active;
    }

    public Cow() {
    }

    public Cow(String number, String name, String mother_number, String father_number, String sex, String color, boolean active) {
        this.number = number;
        this.name = name;
        this.mother_number = mother_number;
        this.father_number = father_number;
        this.sex = sex;
        this.color = color;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Cow{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", birth_date=" + birth_date +
                ", mother_number='" + mother_number + '\'' +
                ", father_number='" + father_number + '\'' +
                ", sex='" + sex + '\'' +
                ", color='" + color + '\'' +
                ", active=" + active +
                ", bloodTests=" + bloodTests +
                ", comments=" + comments +
                ", dewormings=" + dewormings +
                ", estruses=" + estruses +
                ", vaccines=" + vaccines +
                '}';
    }
}
