package pl.mysior.welshblackrestapi.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Cow;

import java.util.List;

@Repository
public interface CowRepository extends MongoRepository<Cow,String> {
}
