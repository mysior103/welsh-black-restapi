package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;

import java.util.List;

public interface DewormingService {

    Cow save(Deworming deworming);

    List<Deworming> findAll();

    List<Deworming> findByCow(String cowNumber);



}
