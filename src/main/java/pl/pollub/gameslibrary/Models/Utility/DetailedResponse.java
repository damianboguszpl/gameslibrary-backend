package pl.pollub.gameslibrary.Models.Utility;

import lombok.Getter;

@Getter
public class DetailedResponse {
    private final String code;
    private final String message;
    private final Object data;

    public DetailedResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
