package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.mysior.welshblackrestapi.security.user.ApplicationUser;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser,Long> {
    ApplicationUser findByUsername(String username);
}
