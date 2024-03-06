package aimeter.proxima.dto;

import aimeter.proxima.exception.validator.CronValidator;
import aimeter.proxima.exception.validator.DateTimeValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static aimeter.proxima.utils.DateTimeFormatConstants.FORMAT_DD_MM_YYY_HH_MM;

public record AIMeterCompleteRegistrationRequest(
        @NotNull(message = "Device id can't be null")
        @JsonProperty("device_id")
        UUID deviceId,

        @NotNull(message = "Device name can't be null")
        @NotBlank(message = "Device name can't be empty")
        @JsonProperty("device_name")
        String deviceName,

        @CronValidator
        @JsonProperty("cron_expr")
        String cronExpression,

        @NotNull(message = "Last execution time can't be null")
        @DateTimeValidator(pattern = FORMAT_DD_MM_YYY_HH_MM)
        @JsonProperty("last_execution_time")
        String lastExecutionTime,

        @NotNull(message = "Time zone can't be null")
        @NotBlank(message = "Time zone can't be empty")
        @JsonProperty("time_zone")
        String timeZone
) {
}
