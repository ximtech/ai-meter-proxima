package aimeter.proxima.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping(value = "/api/data")
@RequiredArgsConstructor
public class AIMeterDataController {

    final Path root = Paths.get("./src/main/resources");

    @PostMapping("/send/{deviceId}")
    public ResponseEntity<String> uploadFileWithoutEntity(@PathVariable("deviceId") String deviceId, 
                                                          @RequestParam("file") MultipartFile file, 
                                                          @RequestParam("battery") int batteryLevel) {
        return null;
    }

}
