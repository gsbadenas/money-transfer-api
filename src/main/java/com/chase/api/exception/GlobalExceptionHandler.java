package com.chase.api.exception;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.chase.api.constant.ErrorCode;
import com.chase.api.dto.ErrorDetails;
import com.chase.api.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception e){
        
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        Map<String,Object> error = Collections.singletonMap("error", errorMsg);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
	
	   @ExceptionHandler(AccountNotExistException.class)
	    public ResponseEntity<?> accounNotFoundException(AccountNotExistException ex, WebRequest request) {
	         ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	         return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	    }
	   
	   @ExceptionHandler(CheckBalanceException.class)
	    public ResponseEntity<?> checkBalanceNotFoundException(CheckBalanceException ex, WebRequest request) {
	         ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	         return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	   
	   @ExceptionHandler(OverDraftException.class)
	    public ResponseEntity<?> overdraftException(OverDraftException ex, WebRequest request) {
	         ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	         return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	
	@ExceptionHandler(SystemException.class)
    public ResponseEntity handleSystemException(HttpServletRequest req, Exception e){
		SystemException sysEx = (SystemException) e;
		
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        log.error("[" + sysEx.getErrorCode() + "] " + sysEx.getMessage());
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(sysEx.getErrorCode());
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
	
	@ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(HttpServletRequest req, Exception e){
		BusinessException businessEx = (BusinessException) e;
		
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        log.error("[" + businessEx.getErrorCode() + "] " + businessEx.getMessage());
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(businessEx.getErrorCode());
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(businessEx.getHttpStatus()).body(response);
    }
	
	@ExceptionHandler(value = { BindException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
	@ResponseBody
	public ResponseEntity handleValidationException(HttpServletRequest req, Exception e){
        
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.CLIENT_ERROR);
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
