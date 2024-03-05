package aimeter.proxima.service;

import aimeter.proxima.domain.entity.AIMeterConfig;
import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.repository.AIMeterConfigRepository;
import aimeter.proxima.domain.repository.AIMeterDeviceRepository;
import aimeter.proxima.dto.AIMeterRegistrationRequest;
import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMeterRegistrationService {

    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final AIMeterConfigRepository aiMeterConfigRepository;
    final GeoIPLocationService geoIPLocationService;
    
    @Transactional
    public GeoIpDTO performDeviceRegistration(String ipAddress, AIMeterRegistrationRequest request) {
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findAIMeterDeviceOrThrow(request.deviceId());
        GeoIpDTO geoIpData = geoIPLocationService.getGeoIpLocationData(ipAddress);
        meterDevice.setRegistered(true);
        meterDevice.setBatteryLevel(request.batteryLevel());
        AIMeterConfig meterConfig = meterDevice.getMeterConfig() == null ? new AIMeterConfig() : meterDevice.getMeterConfig();
        meterConfig.setDeviceIp(geoIpData.ip());
        meterConfig.setDeviceTimeZone(geoIpData.timeZone().name());
        meterDevice.setMeterConfig(meterConfig);
        aiMeterConfigRepository.save(meterConfig);
        aiMeterDeviceRepository.save(meterDevice);
        return geoIpData;
    }
    
    
}
