package pl.mysior.welshblackrestapi.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Cow;
@Repository
public interface CowRepository extends MongoRepository<Cow,String> {
}
