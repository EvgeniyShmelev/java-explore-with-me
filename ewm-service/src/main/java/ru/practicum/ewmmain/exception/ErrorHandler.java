package ru.practicum.ewmmain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotException(final NotFoundException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.FORBIDDEN.toString(),
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleConflictException(ConflictException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.toString(),
                e.getMessage()
        );
    }

}
