package pl.mysior.welshblackrestapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Data
@Document
public class Cow {
    private String name;
    @Id
    private String number;
    private Calendar birth_date;
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

    public Cow(String number, String name, Calendar birth_date, String mother_number, String father_number, String sex, String color, boolean active) {

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
