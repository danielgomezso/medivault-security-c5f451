package ucr.ac.cr.medivault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación en los datos de entrada.");
        problemDetail.setType(URI.create("https://medivault.ucr.ac.cr/errors/bad-request"));
        problemDetail.setTitle("Petición Inválida");

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("errores", errors);
        
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://medivault.ucr.ac.cr/errors/not-found"));
        problemDetail.setTitle("Recurso no encontrado");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setType(URI.create("https://medivault.ucr.ac.cr/errors/forbidden"));
        problemDetail.setTitle("Acceso Denegado");
        return problemDetail;
    }

    // Captura genérica para errores 500 (Evita fuga de Stack Traces internos)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error interno en el servidor.");
        problemDetail.setType(URI.create("https://medivault.ucr.ac.cr/errors/internal-server-error"));
        problemDetail.setTitle("Error Interno del Servidor");
        // Nunca exponemos el ex.getMessage() original aquí por seguridad.
        return problemDetail;
    }
}