package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.security.user.ApplicationUser;

@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser,String> {
    ApplicationUser findByUsername(String username);
}
