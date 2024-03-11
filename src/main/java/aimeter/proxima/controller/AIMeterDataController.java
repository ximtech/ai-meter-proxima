package aimeter.proxima.controller;

import aimeter.proxima.service.AIMeterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/data")
@RequiredArgsConstructor
public class AIMeterDataController {

    final AIMeterDataService aiMeterDataService;

    @PostMapping(value = "/send/{deviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFileWithoutEntity(@PathVariable("deviceId") UUID deviceId,
                                                @RequestPart("file") FilePart filePart,
                                                @RequestParam("battery") int batteryLevel) {
        return filePart.content().flatMap((DataBuffer dataBuffer) -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    aiMeterDataService.handleMeterReceivedData(deviceId, filePart.filename(), bytes, batteryLevel);
                    return Mono.just("Data successfully saved");
                }).last();
    }
}
