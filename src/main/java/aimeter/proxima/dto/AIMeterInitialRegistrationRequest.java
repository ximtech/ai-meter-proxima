package aimeter.proxima.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AIMeterInitialRegistrationRequest(
        @NotNull(message = "Device id can't be null")
        @JsonProperty("device_id")
        UUID deviceId,

        @NotNull(message = "Device name can't be null")
        @NotBlank(message = "Empty device name")
        @JsonProperty("device_name")
        String deviceName,
        
        @NotNull(message = "Battery level can't be null")
        @Min(value = 0, message = "Battery level can't be less than 0%")
        @Max(value = 100, message = "Battery level can't be higher than 100%")
        @JsonProperty("battery_level")
        Integer batteryLevel
) {

}
