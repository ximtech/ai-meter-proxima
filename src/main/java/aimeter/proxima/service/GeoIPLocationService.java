package aimeter.proxima.service;

import aimeter.proxima.dto.GeoIpDTO;
import aimeter.proxima.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoIPLocationService {

    @Value("${ip.geolocation.api.key}")
    String ipGeoLocationServiceApiKey;

    @Qualifier("ipGeoLocationClient")
    final RestClient restClient;

    @Cacheable(cacheNames = "geoIpCache")
    public GeoIpDTO getGeoIpLocationData(String ipAddress) {
        return Optional.ofNullable(restClient.get()
                        .uri((UriBuilder uriBuilder) -> uriBuilder
                                .queryParam("apiKey", ipGeoLocationServiceApiKey)
                                .queryParam("ip", ipAddress)
                                .build())
                        .retrieve()
                        .toEntity(GeoIpDTO.class)
                        .getBody())
                .orElseThrow(() -> new ApiException("Unable to receive geo ip location data from API", HttpStatus.SERVICE_UNAVAILABLE));
    }
}
