package aimeter.proxima.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${ip.geolocation.api.key}")
    String ipGeoLocationServiceApiKey;
    
    @Bean(name = "ipGeoLocationClient")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.ipgeolocation.io/ipgeo?apiKey=%s&fields=time_zone".formatted(ipGeoLocationServiceApiKey))
                .build();
    }
}
