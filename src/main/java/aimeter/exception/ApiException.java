package aimeter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

@Getter
public class ApiException extends RuntimeException {
    
    final String message;
    final HttpStatus status;

    public ApiException(@NonNull String message, @NonNull HttpStatus status) {
        Assert.notNull(message, "Message can't be null");
        Assert.notNull(status, "Http status should not be null");
        this.message = message;
        this.status = status;
    }
}
