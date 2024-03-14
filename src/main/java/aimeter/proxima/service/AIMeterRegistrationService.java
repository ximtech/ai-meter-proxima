package aimeter.proxima.service;

import aimeter.proxima.domain.entity.AIMeterConfig;
import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.repository.AIMeterConfigRepository;
import aimeter.proxima.domain.repository.AIMeterDeviceRepository;
import aimeter.proxima.dto.AIMeterCompleteRegistrationRequest;
import aimeter.proxima.dto.AIMeterInitialRegistrationRequest;
import aimeter.proxima.dto.GeoIpDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static aimeter.proxima.utils.DateTimeFormatConstants.YYYY_MM_DD_HH_MM_FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMeterRegistrationService {

    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final AIMeterConfigRepository aiMeterConfigRepository;
    final GeoIPLocationService geoIPLocationService;
    
    @Transactional
    public GeoIpDTO performDeviceInitialRegistration(String ipAddress, AIMeterInitialRegistrationRequest request) {
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findAIMeterDeviceOrThrow(request.deviceId());
        GeoIpDTO geoIpData = geoIPLocationService.getGeoIpLocationData(ipAddress);
        meterDevice.setRegistered(true);
        meterDevice.setBatteryLevel(request.batteryLevel());
        AIMeterConfig meterConfig = meterDevice.getMeterConfig() == null ? new AIMeterConfig() : meterDevice.getMeterConfig();
        meterConfig.setDeviceName(request.deviceName());
        meterConfig.setDeviceIp(geoIpData.ip());
        meterConfig.setDeviceTimeZone(geoIpData.timeZone().name());
        meterDevice.setMeterConfig(meterConfig);
        aiMeterConfigRepository.save(meterConfig);
        aiMeterDeviceRepository.save(meterDevice);
        return geoIpData;
    }

    public void performDeviceCompleteRegistration(AIMeterCompleteRegistrationRequest request) {
        AIMeterDevice meterDevice = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(request.deviceId());
        AIMeterConfig meterConfig = meterDevice.getMeterConfig();
        meterConfig.setDeviceName(request.deviceName());
        meterConfig.setCronExpression(request.cronExpression());
        LocalDateTime lastJobExecutionDate = LocalDateTime.parse(request.lastExecutionTime(), YYYY_MM_DD_HH_MM_FORMATTER);
        meterConfig.setLastExecutionTime(lastJobExecutionDate);
        meterConfig.setDeviceTimeZone(request.timeZone());
        aiMeterConfigRepository.save(meterConfig);
    }
}
