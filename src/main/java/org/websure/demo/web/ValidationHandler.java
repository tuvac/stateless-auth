package org.websure.demo.web;

import java.net.URI;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class ValidationHandler {

	@Autowired
	private MessageSource msgSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {

		BindingResult result = ex.getBindingResult();
		FieldError error = result.getFieldError();

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.build()
				.toUri();
		
		return processFieldError(error, ex.getClass().getName(), uri.getPath());
	}

	private ErrorDTO processFieldError(FieldError error, String exceptionClass, String path) {

		ErrorDTO dto = null;
		
		if (error != null) {
			
			Locale currentLocale = LocaleContextHolder.getLocale();
			String message = msgSource.getMessage(error.getDefaultMessage(), null, currentLocale);
			
			dto = new ErrorDTO(message, HttpStatus.BAD_REQUEST, exceptionClass, path);

		}
		return dto;
	}

}
