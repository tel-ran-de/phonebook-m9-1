package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.validation.ViolationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ViolationDto> handleDtoValidationException(UserDoesntExistException ex) {
        List<ViolationDto> violations = new ArrayList<>();
        ViolationDto error = new ViolationDto(ex.getMessage());
        violations.add(error);
        return violations;
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ViolationDto> handleArgumentValidationException(TokenNotFoundException ex) {
        List<ViolationDto> violations = new ArrayList<>();
        ViolationDto error = new ViolationDto(ex.getMessage());
        violations.add(error);
        return violations;
    }

//    @ExceptionHandler(TokenNotFoundException.class)
//    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException exception) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", exception.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

}
