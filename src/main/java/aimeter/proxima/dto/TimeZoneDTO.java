package aimeter.proxima.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TimeZoneDTO(
        @JsonProperty("name") String name,
        @JsonProperty("offset") int offset,
        @JsonProperty("offset_with_dst") int offsetWithDst,
        @JsonProperty("current_time") String currentTime,
        @JsonProperty("current_time_unix") double currentTimeUnix,
        @JsonProperty("is_dst") boolean isDst,
        @JsonProperty("dst_savings") int dstSavings
) {

}
