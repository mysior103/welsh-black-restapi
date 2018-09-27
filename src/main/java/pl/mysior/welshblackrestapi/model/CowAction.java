package pl.mysior.welshblackrestapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CowAction implements Comparable<CowAction>{
    private String cowNumber;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actionDate;

    public CowAction(String cowNumber, LocalDate date) {
        this.cowNumber = cowNumber;
        this.actionDate = date;
    }

    public CowAction() {
    }

    public String getCowNumber() {
        return cowNumber;
    }

    public void setCowNumber(String cowNumber) {
        this.cowNumber = cowNumber;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    @Override
    public int compareTo(CowAction o) {
        return getActionDate().compareTo(o.getActionDate());
    }
}
