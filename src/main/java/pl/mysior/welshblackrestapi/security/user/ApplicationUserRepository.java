package pl.mysior.welshblackrestapi.security.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser,String> {
    ApplicationUser findByUsername(String username);
}
