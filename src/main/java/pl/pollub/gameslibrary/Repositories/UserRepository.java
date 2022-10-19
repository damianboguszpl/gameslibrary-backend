package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import pl.pollub.gameslibrary.Models.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
