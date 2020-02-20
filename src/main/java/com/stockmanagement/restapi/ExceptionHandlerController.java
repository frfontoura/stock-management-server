package com.stockmanagement.restapi;

import com.stockmanagement.infra.exception.StockManagementException;
import com.stockmanagement.restapi.dto.DefaultErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author frfontoura
 * @version 1.0 08/01/2020
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorDTO> exception(final Exception e, final HttpServletRequest request, final Locale locale) {
        final DefaultErrorDTO err = DefaultErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(e.getMessage())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .showMessage(false)
                .build();

        logger.error(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler(StockManagementException.class)
    public ResponseEntity<DefaultErrorDTO> stockManagementException(final StockManagementException e, final HttpServletRequest request) {
        logger.debug(e.getMessage(), e);

        final DefaultErrorDTO err = DefaultErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(e.getMessage())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .showMessage(e.isShowMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultErrorDTO> MethodArgumentNotValidException(final MethodArgumentNotValidException e, final HttpServletRequest request) {
        logger.debug(e.getMessage(), e);

        final String allErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (message, fieldMessage) -> fieldMessage + "\n");


        final DefaultErrorDTO err = DefaultErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("Validation Error")
                .message(allErrors)
                .path(request.getRequestURI())
                .showMessage(true)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(err);
    }
}
