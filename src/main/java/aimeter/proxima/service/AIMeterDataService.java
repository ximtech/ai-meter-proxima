package aimeter.proxima.service;

import aimeter.proxima.domain.entity.AIMeterData;
import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.repository.AIMeterDataRepository;
import aimeter.proxima.domain.repository.AIMeterDeviceRepository;
import aimeter.proxima.domain.repository.AIMeterSubscriptionRepository;
import aimeter.proxima.dto.AIMeterDataSendRequest;
import aimeter.proxima.dto.MeterDataQueueMessage;
import aimeter.proxima.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.iron.ironmq.Client;
import io.iron.ironmq.Queue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static aimeter.proxima.utils.DateTimeFormatConstants.IMAGE_FILE_NAME_DATE_TIME_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMeterDataService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @Value("${meter.data.queue.name}")
    String meterDataQueueName;
    
    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final AIMeterSubscriptionRepository aiMeterSubscriptionRepository;
    final AIMeterDataRepository aiMeterDataRepository;
    final Client ironMQClient;
    
    public void handleMeterReceivedData(AIMeterDataSendRequest dataSendRequest) {
        if (dataSendRequest.imageBytes().length == 0) {
            throw new ApiException("Empty file", HttpStatus.BAD_REQUEST);
        }
        
        if (dataSendRequest.imageBytes().length > FileUtils.ONE_MB) {
            throw new ApiException("File size exceeds maximum limit", HttpStatus.BAD_REQUEST);
        }
        
        String fileDateString = StringUtils.substringBetween(dataSendRequest.imageFileName(), "photo_", ".jpeg");
        if (StringUtils.isBlank(fileDateString)) {
            throw new ApiException("Invalid file name", HttpStatus.BAD_REQUEST);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(IMAGE_FILE_NAME_DATE_TIME_FORMAT);
        LocalDateTime fileDateTime = LocalDateTime.parse(fileDateString, formatter);

        AIMeterDevice device = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(dataSendRequest.deviceId());
        boolean isMeterHaveSubscription = aiMeterSubscriptionRepository.existsAIMeterSubscriptionByDevice(device);
        if (!isMeterHaveSubscription) {
            throw new ApiException("Meter with id: [%s] do not have any subscriptions".formatted(device.getDeviceId().toString()), HttpStatus.BAD_REQUEST);
        }
        
        AIMeterData meterData = persistAttachment(device, dataSendRequest, fileDateTime);
        device.setBatteryLevel(dataSendRequest.batteryLevel());
        aiMeterDeviceRepository.save(device);
        proceedMeterDataToQueue(meterData);
    }

    @SneakyThrows
    private AIMeterData persistAttachment(AIMeterDevice device, AIMeterDataSendRequest dataSendRequest, LocalDateTime fileDateTime) {
        AIMeterData meterData = new AIMeterData();
        meterData.setMimeType(dataSendRequest.contentType());
        meterData.setImageName(dataSendRequest.imageFileName());
        meterData.setImageSize(dataSendRequest.imageBytes().length);
        meterData.setImageData(dataSendRequest.imageBytes());
        meterData.setImageDate(fileDateTime);
        meterData.setDevice(device);
        return aiMeterDataRepository.save(meterData);
    }
    
    @SneakyThrows
    private void proceedMeterDataToQueue(AIMeterData meterData) {
        Queue dataQueue = ironMQClient.queue(meterDataQueueName);
        MeterDataQueueMessage dataQueueMessage = new MeterDataQueueMessage(meterData.getId());
        String messageAsJsonString = OBJECT_MAPPER.writeValueAsString(dataQueueMessage);
        dataQueue.push(messageAsJsonString);
    }
}
