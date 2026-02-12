package com.flight.exception;

import com.flight.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {

		List<ApiError> errors = List.of(new ApiError(ErrorCode.INVALID_REQUEST, ex.getMessage()));

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.failure(ErrorCode.INVALID_REQUEST, errors));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {

		List<ApiError> errors = List.of(new ApiError(ErrorCode.INVALID_REQUEST, ex.getMessage()));

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.failure(ErrorCode.INVALID_REQUEST, errors));
	}

	private int resolveFieldErrorCode(String field) {
		int code = getUserFieldErrorCode(field);
		if (code != ErrorCode.VALIDATION_FAILED)
			return code;

		code = getTripFieldErrorCode(field);
		if (code != ErrorCode.VALIDATION_FAILED)
			return code;

		return getFlightFieldErrorCode(field);
	}

	private int getUserFieldErrorCode(String field) {
		return switch (field) {
		case "username" -> ErrorCode.USERNAME_INVALID;
		case "password" -> ErrorCode.PASSWORD_INVALID;
		case "firstName" -> ErrorCode.FIRST_NAME_INVALID;
		case "lastName" -> ErrorCode.LAST_NAME_INVALID;
		case "email" -> ErrorCode.EMAIL_INVALID;
		case "phone" -> ErrorCode.PHONE_NO_INVALID;
		case "wallet" -> ErrorCode.WALLET_INVALID;
		default -> ErrorCode.VALIDATION_FAILED;
		};
	}

	private int getTripFieldErrorCode(String field) {
		return switch (field) {
		case "flightId" -> ErrorCode.TRIP_FLIGHT_ID_INVALID;
		case "from" -> ErrorCode.TRIP_FROM_INVALID;
		case "to" -> ErrorCode.TRIP_TO_INVALID;
		case "departureTime" -> ErrorCode.TRIP_DEPARTURE_TIME_INVALID;
		case "arrivalTime" -> ErrorCode.TRIP_ARRIVAL_TIME_INVALID;
		case "economyFare" -> ErrorCode.TRIP_ECONOMY_FARE_INVALID;
		case "businessFare" -> ErrorCode.TRIP_BUSINESS_FARE_INVALID;
		case "status" -> ErrorCode.TRIP_STATUS_INVALID;
		default -> ErrorCode.VALIDATION_FAILED;
		};
	}

	private int getFlightFieldErrorCode(String field) {
		return switch (field) {
		case "flightNo" -> ErrorCode.FLIGHT_NUMBER_INVALID;
		case "flightName" -> ErrorCode.FLIGHT_NAME_INVALID;
		case "totalEconomySeats" -> ErrorCode.TOTAL_ECONOMY_SEATS_INVALID;
		case "totalBusinessSeats" -> ErrorCode.TOTAL_BUSINESS_SEATS_INVALID;
		case "availableEconomySeats" -> ErrorCode.AVAILABLE_ECONOMY_SEATS_INVALID;
		case "availableBusinessSeats" -> ErrorCode.AVAILABLE_BUSINESS_SEATS_INVALID;
		default -> ErrorCode.VALIDATION_FAILED;
		};
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleOtherExceptions(Exception ex) {

		List<ApiError> errors = List.of(new ApiError(ErrorCode.INTERNAL_ERROR, ex.getMessage()));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.failure(ErrorCode.INTERNAL_ERROR, errors));
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, WebExchangeBindException.class })
	public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(Exception ex) {

		List<FieldError> fieldErrors;
		if (ex instanceof MethodArgumentNotValidException mvcEx) {
			fieldErrors = mvcEx.getBindingResult().getFieldErrors();
		} else if (ex instanceof WebExchangeBindException webEx) {
			fieldErrors = webEx.getFieldErrors();
		} else {
			fieldErrors = List.of();
		}
		List<ApiError> errors = fieldErrors.stream()
				.map(fe -> new ApiError(resolveFieldErrorCode(fe.getField()), fe.getDefaultMessage())).toList();
		if (errors.isEmpty()) {
			errors = List.of(new ApiError(ErrorCode.VALIDATION_FAILED, "Validation failed"));
		}
		int outerResponseCode = errors.get(0).getResponseCode();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(outerResponseCode, errors));
	}

}
