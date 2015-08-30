package org.websure.demo.web;

import java.net.URI;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class DataIntegrityViolationHandler {

	@Autowired
	private MessageSource msgSource;	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody	
	public ErrorDTO processConflictError(DataIntegrityViolationException ex) {
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.build()
				.toUri();
	
		return processExceptionMessage(ex, uri.getPath());
	}

	private ErrorDTO processExceptionMessage(DataIntegrityViolationException ex, String path) {
		
		ErrorDTO dto = null;
		String message = null;
		Locale currentLocale = LocaleContextHolder.getLocale();	
	
		if (ex.getMessage().contains("UK_USERNAME")) {
			
			message = msgSource.getMessage("error.user.username.notunique", null, currentLocale);
			
		} else {
			
			message = msgSource.getMessage("error.data.integrity", null, currentLocale);
			
		}
		
		dto = new ErrorDTO(message, HttpStatus.CONFLICT, ex.getClass().getName(), path);
		
		return dto;
	}
	
}
