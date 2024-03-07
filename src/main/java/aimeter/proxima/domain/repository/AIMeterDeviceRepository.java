package aimeter.proxima.domain.repository;

import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.exception.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public interface AIMeterDeviceRepository extends JpaRepository<AIMeterDevice, Long> {
    
    @Query("""
        FROM AIMeterDevice meter
        LEFT JOIN FETCH meter.meterConfig config
        WHERE meter.deviceId = :deviceId
        AND meter.deleted = FALSE""")
    Optional<AIMeterDevice> findAIMeterDevice(@Param("deviceId") UUID deviceId);
    
    default AIMeterDevice findAIMeterDeviceOrThrow(UUID deviceId) {
        return findAIMeterDevice(deviceId)
                .orElseThrow(() -> new ApiException("Device with id: [%s] do not exist".formatted(deviceId), HttpStatus.NOT_FOUND));
    }

    @Query("""
        FROM AIMeterDevice meter
        JOIN FETCH meter.meterConfig config
        WHERE meter.deviceId = :deviceId
        AND meter.deleted = FALSE
        AND meter.registered = TRUE""")
    Optional<AIMeterDevice> findRegisteredAIMeterDevice(@Param("deviceId") UUID deviceId);

    default AIMeterDevice findRegisteredAIMeterDeviceOrThrow(UUID deviceId) {
        return findRegisteredAIMeterDevice(deviceId)
                .orElseThrow(() -> new ApiException("Device with id: [%s] is not registered or not exist".formatted(deviceId), HttpStatus.NOT_FOUND));
    }

}
