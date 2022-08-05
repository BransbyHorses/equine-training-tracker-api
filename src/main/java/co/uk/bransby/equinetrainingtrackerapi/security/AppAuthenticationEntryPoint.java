package co.uk.bransby.equinetrainingtrackerapi.security;

import co.uk.bransby.equinetrainingtrackerapi.utils.RestApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException
    ) throws IOException {

        RestApiError errorResponseBody = new RestApiError(
               request.getRequestURL(),
                HttpStatus.UNAUTHORIZED,
                authenticationException.getMessage());

        response.addHeader("WWW-Authenticate", ""); // TODO - add www-Authenticate error values
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponseBody));

    }

}
