package aimeter.proxima.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

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
        String ipAddress = getHostIpAddress();
        return StringUtils.isNotEmpty(ipAddress) ? ipAddress :
                IP_HEADER_CANDIDATES.stream()
                .filter((String header) -> {
                    String ipListAsString = request.getHeaders().getFirst(header);
                    return StringUtils.isNotBlank(ipListAsString) && !ipListAsString.equalsIgnoreCase("unknown");
                })
                .map((String ipListAsString) -> ipListAsString.split(",")[0])
                .filter(IP_VALIDATOR::isValid)
                .findFirst()
                .orElse(request.getRemoteAddress().getAddress().getHostAddress());
    }
    
    private static String getHostIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("Unknown IP address: {}", e.toString());
            return null;
        }
    }
}
