package aimeter.proxima.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramOneTimePasswordDTO(
        @JsonProperty("bot_name") String botName,     
        @JsonProperty("pin_code") String pinCode
) {
}
