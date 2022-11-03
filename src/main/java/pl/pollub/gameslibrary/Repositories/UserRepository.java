package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
