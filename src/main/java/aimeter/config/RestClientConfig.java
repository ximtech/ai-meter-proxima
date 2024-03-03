package aimeter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${api.exchange.rate.access.key}")
    String currencyExchangeRateApiKey;
    
    @Bean(name = "exchangeRateClient")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/%s".formatted(currencyExchangeRateApiKey))
                .build();
    }
}
