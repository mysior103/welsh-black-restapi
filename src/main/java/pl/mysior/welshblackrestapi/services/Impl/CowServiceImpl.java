package pl.mysior.welshblackrestapi.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.mysior.welshblackrestapi.exception.CowNotFoundException;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.CowService;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;
import pl.mysior.welshblackrestapi.services.Mapper.CowMapper;

import java.util.List;
import java.util.Optional;

@Service
public class CowServiceImpl implements CowService {

    private final
    CowRepository cowRepository;

    @Autowired
    public CowServiceImpl(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(CowDTO cowDTO) {
        cowRepository.save(CowMapper.toEntity(cowDTO));
    }

    @Override
    public void update(CowDTO cowDTO) throws CowNotFoundException {
        cowRepository.save(CowMapper.toEntity(cowDTO));
    }

    @Override
    public List<Cow> findAll() {
        return cowRepository.findAll();
    }

    @Override
    public CowDTO findByNumber(String number) throws CowNotFoundException {
        Cow foundCow = cowRepository.findById(number).orElseThrow(() -> new CowNotFoundException(number));
        return CowMapper.toDto(foundCow);
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
    public List<Cow> findAllChildren(String motherNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("motherNumber").is(motherNumber));
        return mongoTemplate.find(query, Cow.class);
    }
}
