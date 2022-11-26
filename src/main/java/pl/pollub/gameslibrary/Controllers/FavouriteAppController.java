package pl.pollub.gameslibrary.Controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Exceptions.Exceptions.IncorrectRequestDataException;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Services.FavouriteAppService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="/favapp")
public class FavouriteAppController {
    @Autowired
    private FavouriteAppService favouriteAppService;

    @PostMapping(path = "")
    public ResponseEntity<DetailedResponse> addFavouriteApp(@RequestBody FavouriteAppForm favouriteAppForm) {
        ResponseEntity<DetailedResponse> newFavouriteApp = favouriteAppService.add(favouriteAppForm.getAppId(), favouriteAppForm.getUserId());
        return Objects.requireNonNullElseGet(newFavouriteApp, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse>  updateFavouriteApp(@RequestBody FavouriteAppForm favouriteAppForm, @PathVariable("id") Long id) throws IncorrectRequestDataException {
        return  favouriteAppService.edit(favouriteAppForm.getAppId(), favouriteAppForm.getUserId(), id);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteFavouriteApp(@PathVariable("id") Long id) {
        return favouriteAppService.delete(id);
    }

    @GetMapping(path = "")
    public Iterable<FavouriteApp> getAllFavouriteApps() {
        return favouriteAppService.getAll();
    }

    @GetMapping (path = "/{id}")
    public FavouriteApp getFavouriteAppById(@PathVariable("id") Long id) {
        return favouriteAppService.getById(id);
    }

    @GetMapping (path = "/user/{id}")
    public ResponseEntity<Iterable<FavouriteApp>> getFavouriteAppByUserId(@PathVariable("id") Long id) {
        List<FavouriteApp> favouriteApps = (List<FavouriteApp>) favouriteAppService.getByUserId(id);
        if (!favouriteApps.isEmpty()) return ResponseEntity.ok().body(favouriteApps);
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

@Data
@AllArgsConstructor
class FavouriteAppForm {
    private Long appId;
    private Long userId;
}
