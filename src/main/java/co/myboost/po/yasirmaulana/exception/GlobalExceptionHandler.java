package co.myboost.po.yasirmaulana.exception;

import co.myboost.po.yasirmaulana.dto.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<WebResponse<Void>> handleNotFoundException(NotFoundException ex) {
        WebResponse<Void> response = WebResponse.<Void>builder()
                .status("ERROR")
                .code("404")
                .message("Data not found: " + ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<Void>> handleGeneralException(Exception ex) {
        WebResponse<Void> response = WebResponse.<Void>builder()
                .status("ERROR")
                .code("500")
                .message("An error occurred: " + ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
