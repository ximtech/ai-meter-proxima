package aimeter.proxima.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimePasswordService {
    
    private static final Random RANDOM = new Random();
    
    public String createDigitPasswordOfLength(int length) {
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomNumber = RANDOM.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return oneTimePassword.toString().trim();
    }
}
