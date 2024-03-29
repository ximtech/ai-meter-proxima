package aimeter.proxima.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {
    
    static final InetAddressValidator IP_VALIDATOR = InetAddressValidator.getInstance();
    static final List<String> IP_HEADER_CANDIDATES = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    );

    public static String getIPFromRequest(ServerHttpRequest request) {
        return IP_HEADER_CANDIDATES.stream()
                .filter((String header) -> {
                    String ipListAsString = request.getHeaders().getFirst(header);
                    return StringUtils.isNotBlank(ipListAsString) && !ipListAsString.equalsIgnoreCase("unknown");
                })
                .map((String header) -> request.getHeaders().getFirst(header).split(",")[0])
                .filter(IP_VALIDATOR::isValid)
                .findFirst()
                .orElse(Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress());
    }
}
