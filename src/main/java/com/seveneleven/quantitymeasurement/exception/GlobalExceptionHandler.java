package com.seveneleven.quantitymeasurement.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
	
	static class ErrorResponse {
        public LocalDateTime timestamp;
        public int status;
        public String error;
        public String message;
        public String path;
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Extract all field/object error messages from the binding result
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        List<String> errorMessages = errorList.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        String combinedMessage = String.join("; ", errorMessages);

        logger.warning("Validation failed: " + combinedMessage +
                       " for request: " + request.getRequestURI());

        ErrorResponse error = new ErrorResponse();
        error.timestamp = LocalDateTime.now();
        error.status    = HttpStatus.BAD_REQUEST.value();
        error.error     = "Quantity Measurement Error";
        error.message   = combinedMessage;
        error.path      = request.getRequestURI();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<ErrorResponse> handleQuantityException(
            QuantityMeasurementException ex,
            HttpServletRequest request) {

        logger.warning("QuantityMeasurementException: " + ex.getMessage() +
                       " for request: " + request.getRequestURI());

        ErrorResponse error = new ErrorResponse();
        error.timestamp = LocalDateTime.now();
        error.status    = HttpStatus.BAD_REQUEST.value();
        error.error     = "Quantity Measurement Error";
        error.message   = ex.getMessage();
        error.path      = request.getRequestURI();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        // Log full stack trace server-side for debugging
        logger.severe("Unexpected exception: " + ex.getMessage() +
                      " for request: " + request.getRequestURI());

        ErrorResponse error = new ErrorResponse();
        error.timestamp = LocalDateTime.now();
        error.status    = HttpStatus.INTERNAL_SERVER_ERROR.value();
        error.error     = "Internal Server Error";
        error.message   = ex.getMessage();
        error.path      = request.getRequestURI();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
}
