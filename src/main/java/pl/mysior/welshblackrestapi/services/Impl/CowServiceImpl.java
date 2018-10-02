package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.CowService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CowServiceImpl implements CowService {

    private final
    CowRepository cowRepository;

    @Autowired
    public CowServiceImpl(CowRepository cowRepository, MongoTemplate mongoTemplate) {
        this.cowRepository = cowRepository;
        this.mongoTemplate = mongoTemplate;
    }

    private final MongoTemplate mongoTemplate;

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
        return cowOptional.orElse(null);
    }

    @Override
    public Cow deleteByNumber(String number) {

        Optional<Cow> cowOptional = cowRepository.findById(number);
        if (cowOptional.isPresent()) {
            cowRepository.deleteById(number);
            return cowOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Cow> findAllChildren(String parentNumber) {
        Criteria motherCriteria = Criteria.where("motherNumber").is(parentNumber);
        Criteria fatherCriteria = Criteria.where("fatherNumber").is(parentNumber);
        Query query = new Query(new Criteria().orOperator(motherCriteria,fatherCriteria));
        return mongoTemplate.find(query, Cow.class);
    }


}
