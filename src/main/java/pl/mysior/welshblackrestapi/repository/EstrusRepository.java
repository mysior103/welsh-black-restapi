package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Estrus;
@Repository
public interface EstrusRepository extends MongoRepository<Estrus,String> {
}
