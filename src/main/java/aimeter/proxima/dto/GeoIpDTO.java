package aimeter.proxima.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeoIpDTO(
        @JsonProperty("ip") String ip,
        @JsonProperty("time_zone") TimeZoneDTO timeZone
) {
}
