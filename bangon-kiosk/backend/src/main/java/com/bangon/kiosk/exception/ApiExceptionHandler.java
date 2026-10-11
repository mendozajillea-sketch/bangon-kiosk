package com.bangon.kiosk.exception;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.security.access.AccessDeniedException;import org.springframework.security.authentication.BadCredentialsException;import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler({IllegalArgumentException.class,NoSuchElementException.class,MethodArgumentNotValidException.class}) ResponseEntity<?> bad(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e instanceof MethodArgumentNotValidException?"Invalid request fields":e.getMessage()));}
 @ExceptionHandler(BadCredentialsException.class) ResponseEntity<?> credentials(Exception e){return ResponseEntity.status(401).body(Map.of("error","Invalid email or password"));}
 @ExceptionHandler(AccessDeniedException.class) ResponseEntity<?> denied(Exception e){return ResponseEntity.status(403).body(Map.of("error","Access denied"));}
}
