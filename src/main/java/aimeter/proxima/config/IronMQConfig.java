package aimeter.proxima.config;

import io.iron.ironmq.Client;
import io.iron.ironmq.Cloud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class IronMQConfig {
    
    @Value("${ironmq.project.id}")
    String ironMQProjectId;

    @Value("${ironmq.token}")
    String ironMQToken;
    
    @Value("${ironmq.host}")
    String ironMQHost;
    
    @Bean
    public Client getIronMQClient() {
        log.info("Starting IronMQ configuration for host: [{}]", ironMQHost);
        Cloud cloud = new Cloud("https", ironMQHost, 443);
        return new Client(ironMQProjectId, ironMQToken, cloud);
    }
}
