package com.stockmanagement.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class DefaultErrorDTO {

	private final Long timestamp = new Date().getTime();
	private final HttpStatus status;
	private final String error;
	private final String message;
	private final String path;

}