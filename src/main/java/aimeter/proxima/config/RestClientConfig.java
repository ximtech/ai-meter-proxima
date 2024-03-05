package aimeter.proxima.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    
    @Bean(name = "ipGeoLocationClient")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.ipgeolocation.io/ipgeo")
                .build();
    }
}
