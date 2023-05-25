package ru.kpfu.itis.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class MethodArgumentTypeMismatchExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handle(HttpServletResponse response) throws IOException {
        response.sendError(400, "/error");
    }
}
