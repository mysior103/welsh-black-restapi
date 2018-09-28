package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.CowAction;
import pl.mysior.welshblackrestapi.services.Impl.CowActionService;

import java.util.List;

public interface BloodTestService {
    Cow save(BloodTest action);

    List<BloodTest> findAll();

    List<BloodTest> findLast();

    List<BloodTest> findByCow(String cowNumber);
}
