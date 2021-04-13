// Глобальная ловушка исключений.
// На данный момент в случае ошибки клиенту прилетит полная трассировка.
// Возможно нужно уведомлять клиента каким-то другим способом. Из задания неочевидно.

package ru.akulin.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleException(RuntimeException cause, WebRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        String bodyOfResponse = "Запрос завершился с ошибкой.\n\n" + sw.toString();
        return handleExceptionInternal(cause, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
