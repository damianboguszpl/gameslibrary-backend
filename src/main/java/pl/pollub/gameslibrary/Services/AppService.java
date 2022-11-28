package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

@Service
public interface AppService {
    ResponseEntity<DetailedResponse> add(App app);
    ResponseEntity<DetailedResponse> edit(App newApp, Long id);
    ResponseEntity<DetailedResponse> delete(Long id);
    Iterable<App> getAll();
    App getById(Long id);
    Iterable<App> getByType(String type);
}
