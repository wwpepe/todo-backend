package backend.likelion.todos.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handle(BadRequestException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ExceptionResponse> handle(UnAuthorizedException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage()),
                HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handle(ForbiddenException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage()),
                HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(NotFoundException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage()),
                HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handle(ConflictException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage()),
                HttpStatusCode.valueOf(HttpStatus.CONFLICT.value())
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionResponse> handle(InternalServerErrorException e) {
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(e.getMessage()));
    }
}
