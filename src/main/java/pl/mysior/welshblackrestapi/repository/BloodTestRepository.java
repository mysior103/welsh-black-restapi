package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.BloodTest;
@Repository
public interface BloodTestRepository extends MongoRepository<BloodTest,String> {
}
