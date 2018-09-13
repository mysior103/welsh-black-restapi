package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.BloodTestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BloodTestServiceImpl implements BloodTestService {

    @Autowired
    private CowRepository cowRepository;

    @Override
    public Cow save(BloodTest bloodTest) {
        Cow foundCow;
        Optional<Cow> optCow = cowRepository.findById(bloodTest.getCowNumber());
        if (optCow.isPresent()) {
            foundCow = optCow.get();
            List<BloodTest> bloodTestList = foundCow.getBloodTests();
            if (bloodTestList != null) {
                bloodTestList.add(bloodTest);
            } else {
                bloodTestList = new ArrayList<>();
                bloodTestList.add(bloodTest);
            }
            foundCow.setBloodTests(bloodTestList);
            return foundCow;
        } else {
            return null;
        }
    }

    @Override
    public List<BloodTest> findAll() {
        List<Cow> allCows = cowRepository.findAll();
        List<BloodTest> allBloodTests = new ArrayList<>();
        for (Cow c : allCows) {
            if (c.getBloodTests() != null) {
                allBloodTests.addAll(c.getBloodTests());
            }
        }
        allBloodTests.sort(BloodTest::compareTo);
        return allBloodTests;
    }

    @Override
    public List<BloodTest> findLast() {
        List<BloodTest> allBloodTests = findAll();
        Collections.reverse(allBloodTests);

        List<BloodTest> lastBloodTest = new ArrayList<>();

        for (BloodTest bt : allBloodTests) {
            if (!containsName(lastBloodTest, bt.getCowNumber())) {
                lastBloodTest.add(bt);
            }
        }
        return lastBloodTest;
    }

    @Override
    public List<BloodTest> findByCow(String cowNumber) {
        Optional<Cow> optionalCow = cowRepository.findById(cowNumber);
        Cow c = optionalCow.orElse(null);
        List<BloodTest> bloodTests = new ArrayList<>();
        if (c.getBloodTests() != null) {
            bloodTests = c.getBloodTests();
            bloodTests.sort(BloodTest::compareTo);
        }
        return bloodTests;
    }

    private boolean containsName(final List<BloodTest> list, final String number) {
        return list.stream().filter(o -> o.getCowNumber().equals(number)).findFirst().isPresent();
    }
}
