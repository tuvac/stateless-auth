package org.websure.demo.web;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorDTO {

	private Date timestamp;
	
	private Integer status;
	
	private String error;
	
	private String exception;
	
	private String message;
	
	private String path;

	public ErrorDTO() {
		
	}

	
	
	public ErrorDTO(String message, HttpStatus httpStatus, String exceptionClass, String path) {
		
		this.timestamp = new Date();
		this.status = httpStatus.value();
		this.error = httpStatus.getReasonPhrase();
		this.exception = exceptionClass;
		this.message = message;
		this.path = path;
	}



	public Date getTimestamp() {
	
		return timestamp;
	}

	public Integer getStatus() {
	
		return status;
	}

	
	public void setStatus(Integer status) {
	
		this.status = status;
	}

	
	public String getError() {
	
		return error;
	}

	
	public void setError(String error) {
	
		this.error = error;
	}

	
	public String getException() {
	
		return exception;
	}

	
	public void setException(String exception) {
	
		this.exception = exception;
	}

	
	public String getMessage() {
	
		return message;
	}

	
	public void setMessage(String message) {
	
		this.message = message;
	}

	
	public String getPath() {
	
		return path;
	}

	
	public void setPath(String path) {
	
		this.path = path;
	}




}
