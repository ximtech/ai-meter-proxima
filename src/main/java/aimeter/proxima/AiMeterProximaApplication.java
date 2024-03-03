package aimeter.proxima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AiMeterProximaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMeterProximaApplication.class, args);
    }

}
