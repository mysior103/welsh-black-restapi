package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Vaccine;

import java.util.List;

public interface VaccineService {
    Cow save(Vaccine vaccine);

    List<Vaccine> findAll();

    List<Vaccine> findByCow(String cowNumber);

}
