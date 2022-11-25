package pl.pollub.gameslibrary.Models.Utility;

import lombok.Getter;
import pl.pollub.gameslibrary.Models.UserDto;

@Getter
public class DetailedUserResponse {
    private final String code;
    private final String message;
    private final UserDto data;

    public DetailedUserResponse(String code, String message, UserDto data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
