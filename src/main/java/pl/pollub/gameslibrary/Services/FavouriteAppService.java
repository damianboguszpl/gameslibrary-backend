package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Exceptions.Exceptions.IncorrectRequestDataException;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

@Service
public interface FavouriteAppService {
    Iterable<FavouriteApp> getAll();
    FavouriteApp getById(Long id);
    ResponseEntity<DetailedResponse> edit(Long appId, Long userId, Long id) throws IncorrectRequestDataException;
    ResponseEntity<DetailedResponse> add(Long appId, Long userId);
    ResponseEntity<DetailedResponse> delete(Long id);
    Iterable<FavouriteApp> getByUserId(Long id);
}
