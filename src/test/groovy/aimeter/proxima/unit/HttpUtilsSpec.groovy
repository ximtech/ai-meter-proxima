package aimeter.proxima.unit

import aimeter.proxima.utils.HttpUtils
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import spock.lang.Specification

class HttpUtilsSpec extends Specification {

    def 'getIPFromRequest() - should correctly resolve ip address from request'() {
        expect:
        def request = MockServerHttpRequest.get('/')
                .remoteAddress(new InetSocketAddress('127.0.0.1', 8080))
                .header(headerName, headerValue)
                .build()
        HttpUtils.getIPFromRequest(request) == ip

        where:
        headerName                 | headerValue  | ip
        'X-Forwarded-For'          | '127.0.0.2'  | '127.0.0.2'
        'Proxy-Client-IP'          | '127.0.0.3'  | '127.0.0.3'
        'WL-Proxy-Client-IP'       | '127.0.0.4'  | '127.0.0.4'
        'HTTP_X_FORWARDED_FOR'     | '127.0.0.5'  | '127.0.0.5'
        'HTTP_X_FORWARDED'         | '127.0.0.6'  | '127.0.0.6'
        'HTTP_X_CLUSTER_CLIENT_IP' | '127.0.0.7'  | '127.0.0.7'
        'HTTP_CLIENT_IP'           | '127.0.0.8'  | '127.0.0.8'
        'HTTP_FORWARDED_FOR'       | '127.0.0.9'  | '127.0.0.9'
        'HTTP_FORWARDED'           | '127.0.0.10' | '127.0.0.10'
        'HTTP_VIA'                 | '127.0.0.11' | '127.0.0.11'
        'REMOTE_ADDR'              | '127.0.0.12' | '127.0.0.12'
        'X-Real-IP'                | '127.0.0.13' | '127.0.0.13'
        'Unknown'                  | '127.0.0.14' | '127.0.0.1'
    }
}
