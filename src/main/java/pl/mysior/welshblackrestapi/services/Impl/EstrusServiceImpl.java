package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.EstrusService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public List<Estrus> findByCow(String cowNumber) {
        return null;
    }
}
