package aimeter.proxima.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AIMeterSubscriptionDTO(
        @JsonProperty("display_name") String displayName,     
        @JsonProperty("link") String link,
        @JsonProperty("icon_link") String iconLink
) {
}
