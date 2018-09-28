package pl.mysior.welshblackrestapi.services.Impl;

import pl.mysior.welshblackrestapi.model.Cow;

import java.util.List;

public interface CowActionService<T> {
    Cow save(T action);

    List<T> findAll();

    List<T> findLast();

    List<T> findByCow(String cowNumber);
}
