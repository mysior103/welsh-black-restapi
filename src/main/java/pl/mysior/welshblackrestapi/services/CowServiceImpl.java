package pl.mysior.welshblackrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CowServiceImpl implements CowService {

    final
    CowRepository cowRepository;

    @Autowired
    public CowServiceImpl(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }


    @Override
    public Cow save(Cow cow) {
        cowRepository.save(cow);
        return cow;
    }

    @Override
    public List<Cow> findAll() {
        return cowRepository.findAll();
    }

    @Override
    public Cow findByNumber(String number) {
        Optional<Cow> cowOptional = cowRepository.findById(number);
        if(cowOptional.isPresent()){
            return cowOptional.get();
        }else{
            return null;
        }
    }

    @Override
    public Cow deleteByNumber(String number) {
        Optional<Cow> cowOptional = cowRepository.findById(number);
        if(cowOptional.isPresent()){
            cowRepository.deleteById(number);
            return cowOptional.get();
        }else{
            return null;
        }
    }
}
