package vn.nlu.huypham.app.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.nlu.huypham.app.exception.custom.*;
import vn.nlu.huypham.app.payload.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler
{

	@ExceptionHandler(exception = NoResourceFoundException.class)
	public ResponseEntity<?> handleNotFound(
		NoResourceFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.builder().code(404).message("Resource not found").build());
	}

	@ExceptionHandler(RedisException.class)
	public ResponseEntity<?> handle(
		RedisException ex)
	{
		return ResponseEntity.status(HttpStatus.OK).body(
				ApiResponse.builder().code(ex.getCode()).message("Unknown error occurred").build());
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> handle(
		AppException ex)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.builder().code(ex.getCode()).message(ex.getMessage()).build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handle(
		MethodArgumentNotValidException ex)
	{

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((
			error) ->
		{
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
				.code(400).message("Field validation error").data(errors).build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handle(
		Exception ex)
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.builder().code(500).message("Unknown error occurred").build());
	}
}
