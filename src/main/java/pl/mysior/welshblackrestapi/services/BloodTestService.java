package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;

import java.util.List;

public interface BloodTestService {
    Cow save(BloodTest bloodTest);

    List<BloodTest> findAll();

    List<BloodTest> findLast();

    List<BloodTest> findByCow(String cowNumber);
}
