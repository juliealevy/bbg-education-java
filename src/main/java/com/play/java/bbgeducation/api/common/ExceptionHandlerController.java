package com.play.java.bbgeducation.api.common;

import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.application.exceptions.InvalidEmailFormatException;
import com.play.java.bbgeducation.application.exceptions.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(InvalidEmailFormatException.class)
    ProblemDetail handleInvalidEmailFormatException(InvalidEmailFormatException ex){
        logger.trace("Invalid email format: " +  ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Invalid Email Format");
        return problemDetail;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    ProblemDetail handleInvalidPasswordException(InvalidPasswordException ex){
        logger.trace("Invalid password: " +  ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Invalid Password");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDeniedException(AccessDeniedException ex){
        logger.trace("Access Denied: " +  ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("Access Denied");
        return problemDetail;
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    ProblemDetail handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex){
        logger.trace("Data access error: " +  ex.getMessage(),ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "See log for details");
        problemDetail.setTitle("Data Access Error");
        return problemDetail;
    }
    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail BadCredentialsException(BadCredentialsException ex){
        logger.trace(ex.getMessage());
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle("Bad Credentials");
        return problemDetail;
    }

    @ExceptionHandler(NoSuchMethodException.class)
    ProblemDetail handleNoSuchMethodException(NoSuchMethodException ex){
        logger.error(ex.getMessage());
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "No such method: " +  ex.getMessage());
        problemDetail.setTitle("Internal Error");
        return problemDetail;
    }

    @ExceptionHandler(InvalidApiEndpointLinkException.class)
    ProblemDetail handleInvalidApiEndpointLinkException(InvalidApiEndpointLinkException ex){
        logger.error(ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Internal Error");
        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    ProblemDetail handleRuntimeException(RuntimeException ex){
        logger.error("Internal Service Error: " +  ex.getMessage(), ex);
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "See server log for details");
        problemDetail.setTitle("Internal Error");
        return problemDetail;
    }

}
