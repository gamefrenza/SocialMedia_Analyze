package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(WeiboScraperException.class)
    public ResponseEntity<String> handleWeiboScraperException(WeiboScraperException e) {
        logger.error("Weibo scraping error: ", e);
        // Don't expose internal error details to client
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred while processing your request");
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        logger.error("Security violation: ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Access denied");
    }
} 