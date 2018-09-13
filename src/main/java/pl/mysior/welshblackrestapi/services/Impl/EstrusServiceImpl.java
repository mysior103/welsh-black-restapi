package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.EstrusService;

import java.util.*;

@Service
public class EstrusServiceImpl implements EstrusService {
    @Autowired
    private CowRepository cowRepository;

    @Override
    public Cow save(Estrus estrus) {
        Optional<Cow> optionalCow = cowRepository.findById(estrus.getCowNumber());
        Cow cow = optionalCow.orElse(null);
        if (cow != null) {
            List<Estrus> estrusList = cow.getEstruses();
            if (estrusList == null) {
                estrusList = new ArrayList<>(Arrays.asList(estrus));
                cow.setEstruses(estrusList);
            } else {
                cow.getEstruses().add(estrus);
            }
        }
        return cow;
    }

    @Override
    public List<Estrus> findAll() {
        List<Cow> allCows = cowRepository.findAll();
        List<Estrus> allEstruses = new ArrayList<>();
        for (Cow c : allCows) {
            if (c.getEstruses() != null && !c.getEstruses().isEmpty()) {
                allEstruses.addAll(c.getEstruses());
            }
        }
        allEstruses.sort(Estrus::compareTo);
        return allEstruses;
    }

    @Override
    public List<Estrus> findLast() {
        List<Estrus> allEstruses = findAll();
        Collections.reverse(allEstruses);

        List<Estrus> lastEstruses = new ArrayList<>();

        for (Estrus e : allEstruses) {
            if (!containsName(lastEstruses, e.getCowNumber())) {
                lastEstruses.add(e);
            }
        }
        return lastEstruses;
    }

    @Override
    public List<Estrus> findByCow(String cowNumber) {
        Optional<Cow> optionalCow = cowRepository.findById(cowNumber);
        Cow c = optionalCow.orElse(null);
        List<Estrus> estruses = new ArrayList<>();
        if (c.getEstruses() != null) {
            estruses = c.getEstruses();
            estruses.sort(Estrus::compareTo);
        }
        return estruses;
    }

    private boolean containsName(final List<Estrus> list, final String number) {
        return list.stream().filter(o -> o.getCowNumber().equals(number)).findFirst().isPresent();
    }
}
