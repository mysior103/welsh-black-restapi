package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Vaccine;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.VaccineService;

import java.util.*;

@Service
public class VaccineServiceImpl implements VaccineService {

    @Autowired
    private CowRepository cowRepository;

    @Override
    public Cow save(Vaccine vaccine) {
        Optional<Cow> optionalCow = cowRepository.findById(vaccine.getCowNumber());
        if (optionalCow.isPresent()) {
            Cow cow = optionalCow.orElse(null);
            List<Vaccine> vaccinesList = cow.getVaccines();
            if (vaccinesList == null) {
                vaccinesList = new ArrayList<>();
                vaccinesList.add(vaccine);
            } else {
                vaccinesList.add(vaccine);
            }
            cow.setVaccines(vaccinesList);
            return cow;
        } else {
            return null;
        }
    }

    @Override
    public List<Vaccine> findAll() {
        List<Cow> allCows = cowRepository.findAll();
        List<Vaccine> allVaccines = new ArrayList<>();
        for(Cow c : allCows){
            if(c.getVaccines()!=null && !c.getVaccines().isEmpty()){
                allVaccines.addAll(c.getVaccines());
            }
        }
        allVaccines.sort(Vaccine::compareTo);
        return allVaccines;
    }

    @Override
    public List<Vaccine> findLast() {
        List<Vaccine> allVaccines = findAll();
        Collections.reverse(allVaccines);

        List<Vaccine> lastVaccines = new ArrayList<>();

        for (Vaccine v : allVaccines){
            if(!containsName(lastVaccines,v.getCowNumber())){
                lastVaccines.add(v);
            }
        }
        return lastVaccines;
    }

    @Override
    public List<Vaccine> findByCow(String cowNumber) {
        Optional<Cow> optionalCow = cowRepository.findById(cowNumber);
        Cow c = optionalCow.orElse(null);
        List<Vaccine> vaccines = new ArrayList<>();
        if(c.getVaccines()!=null && !c.getVaccines().isEmpty()){
            vaccines = c.getVaccines();
            vaccines.sort(Vaccine::compareTo);
        }
        return vaccines;
    }

    private boolean containsName(final List<Vaccine> list, final String number) {
        return list.stream().filter(o -> o.getCowNumber().equals(number)).findFirst().isPresent();
    }
}
