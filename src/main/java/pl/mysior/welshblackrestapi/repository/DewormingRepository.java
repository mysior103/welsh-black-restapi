package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Deworming;
@Repository
public interface DewormingRepository extends MongoRepository<Deworming,String> {
}
