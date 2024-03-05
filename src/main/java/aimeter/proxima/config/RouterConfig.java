package aimeter.proxima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {
    
    @Bean
    public RouterFunction<ServerResponse> getRootRedirectRout() {
        return RouterFunctions.route()
                .GET("/", accept(MediaType.TEXT_PLAIN), 
                        (ServerRequest request) -> 
                                ServerResponse.permanentRedirect(URI.create("/actuator/health")).bodyValue("")).build();
    }
}
