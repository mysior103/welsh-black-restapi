package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.CowActionService;

import java.util.*;

@Service
public class DewormingServiceImpl implements CowActionService<Deworming> {

    @Autowired
    CowRepository cowRepository;


    @Override
    public Cow save(Deworming deworming) {
        Optional<Cow> optionalCow = cowRepository.findById(deworming.getCowNumber());
        Cow cow = optionalCow.orElse(null);

        if (cow != null) {
            List<Deworming> dewormingList = cow.getDewormings();
            if (dewormingList == null) {
                dewormingList = new ArrayList<>(Arrays.asList(deworming));
                cow.setDewormings(dewormingList);
            } else {
                cow.getDewormings().add(deworming);
            }
        }
        return cow;
    }

    @Override
    public List<Deworming> findAll() {
        List<Cow> cowList = cowRepository.findAll();
        List<Deworming> dewormingList = new ArrayList<>();
        for (Cow c : cowList) {
            if (c.getDewormings() != null) {
                dewormingList.addAll(c.getDewormings());
            }
        }
        dewormingList.sort(Deworming::compareTo);
        Collections.reverse(dewormingList);
        return dewormingList;
    }

    @Override
    public List<Deworming> findLast() {
        List<Deworming> allDewormings = findAll();
        Collections.reverse(allDewormings);

        List<Deworming> lastDeworming = new ArrayList<>();

        for (Deworming d : allDewormings) {
            if (!containsName(lastDeworming, d.getCowNumber())) {
                lastDeworming.add(d);
            }
        }
        return lastDeworming;
    }

    @Override
    public List<Deworming> findByCow(String cowNumber) {
        List<Deworming> dewormingList = new ArrayList<>();
        Optional<Cow> optionalCow = cowRepository.findById(cowNumber);
        Cow cow = optionalCow.orElse(null);
        if (cow == null) {
            return null;
        }
        if (cow.getDewormings() != null) {
            dewormingList.addAll(cow.getDewormings());
        }
        return dewormingList;
    }

    private boolean containsName(final List<Deworming> list, final String number) {
        return list.stream().filter(o -> o.getCowNumber().equals(number)).findFirst().isPresent();
    }
}
