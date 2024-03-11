package aimeter.proxima.dto;

import java.util.UUID;

public record AIMeterDataSendRequest(
        UUID deviceId,
        String imageFileName,
        byte[] imageBytes,
        int batteryLevel,
        String contentType
) {
}
