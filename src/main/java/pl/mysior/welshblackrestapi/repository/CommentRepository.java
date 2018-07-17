package pl.mysior.welshblackrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mysior.welshblackrestapi.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
}
