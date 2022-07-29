package co.uk.bransby.equinetrainingtrackerapi.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class RestApiError {

    private final StringBuffer requestUrl;
    private final HttpStatus status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private String errorMessage;

    RestApiError(StringBuffer requestUrl, HttpStatus status, String errorMessage) {
        this.requestUrl = requestUrl;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

}
