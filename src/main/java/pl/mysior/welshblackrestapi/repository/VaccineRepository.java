package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Vaccine;
@Repository
public interface VaccineRepository extends MongoRepository<Vaccine,String> {
}
