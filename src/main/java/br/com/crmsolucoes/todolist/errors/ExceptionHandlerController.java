package br.com.crmsolucoes.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //Define uma classe que é verificada antes de tudo quando há uma Exceção (Assim como o Filter em rota)
public class ExceptionHandlerController {
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotRedableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMostSpecificCause().getMessage());
    }
}
