package pl.pollub.gameslibrary.Services;

import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;

@Service
public interface AppService {
    Iterable<App> findAll();
    App findById(Long id);
    App edit(App newApp, Long id);
    App add(App app);
    App del(Long id);
}
