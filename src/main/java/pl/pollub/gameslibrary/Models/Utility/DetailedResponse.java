package pl.pollub.gameslibrary.Models.Utility;

import lombok.Getter;
import pl.pollub.gameslibrary.Models.UserDto;

@Getter
public class DetailedResponse {
    private final String code;
    private final String message;
    private final UserDto data;

    public DetailedResponse(String code, String message, UserDto data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
