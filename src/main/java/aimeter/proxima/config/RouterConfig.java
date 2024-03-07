package aimeter.proxima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {
    
    public static final String IMAGES_ROOT_PATH = "images";
    
    @Bean
    public RouterFunction<ServerResponse> getRootRedirectRout() {
        return RouterFunctions.route()
                .GET("/", accept(MediaType.TEXT_PLAIN), 
                        (ServerRequest request) -> 
                                ServerResponse.permanentRedirect(URI.create("/actuator/info")).bodyValue("")).build();
    }

    @Bean
    public RouterFunction<ServerResponse> imageRout() {
        return RouterFunctions.resources("/" + IMAGES_ROOT_PATH + "/**", new ClassPathResource("static/assets/img/"));
    }
}
