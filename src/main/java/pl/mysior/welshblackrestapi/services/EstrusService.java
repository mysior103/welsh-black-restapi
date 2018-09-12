package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;

import java.util.List;

public interface EstrusService {
    Cow save(Estrus estrus);

    List<Estrus> findAll();

    List<Estrus> findByCow(String cowNumber);

}
