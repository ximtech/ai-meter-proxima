package aimeter.proxima.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandlingAdvice {

    @ExceptionHandler({ApiException.class})  // custom exception handler
    public Mono<ResponseEntity<Map<String, String>>> handleCustomExceptions(ApiException e) {
        return Mono.just(ResponseEntity.status(e.getStatus()))
                .flatMap(status ->
                        Mono.deferContextual(Mono::just).map(context -> {
                            log.error(e.getMessage());
                            return status.body(Map.of("error", e.getMessage()));
                        })).publishOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler({WebExchangeBindException.class}) // hibernate validation exceptions
    public Mono<ResponseEntity<Object>> handleBeanValidationExceptions(WebExchangeBindException exception) {
        return Mono.deferContextual(Mono::just).map(context -> {
            Map<String, String> errorDetails = new HashMap<>();
            exception.getFieldErrors().forEach((FieldError error) -> errorDetails.put(error.getField(), error.getDefaultMessage()));
            exception.getGlobalErrors().forEach((ObjectError error) -> errorDetails.put(error.getObjectName(), error.getDefaultMessage()));
            return new ResponseEntity<Object>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }).publishOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler({ResponseStatusException.class})  // json binding and other framework exceptions
    public Mono<ResponseEntity<Map<String, String>>> handleNestedExceptions(ResponseStatusException e) {
        return Mono.just(ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", Objects.requireNonNull(e.getReason()))))
                .publishOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler({Exception.class})    // internal errors
    public Mono<ResponseEntity<Map<String, String>>> handleDefaultInternalExceptions(Exception e) {
        return Mono.fromCallable(() -> {
            String message = "Internal server error:";
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(message, e.getMessage()));
        }).publishOn(Schedulers.boundedElastic());
    }

}
