package com.flight.controller;

import com.flight.dto.FlightDto;
import com.flight.service.FlightService;
import com.flight.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

	@Autowired
	private FlightService flightService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addFlight")
	public ResponseEntity<ApiResponse<?>> addFlight(@Valid @RequestBody FlightDto flightDto) {

		FlightDto saved = flightService.createOrUpdateFlight(flightDto);

		List<SuccessMessage> messages = List.of(new SuccessMessage(
				flightDto.getId() == null ? "Flight created successfully" : "Flight updated successfully"));

		return ResponseEntity.status(flightDto.getId() == null ? HttpStatus.CREATED : HttpStatus.OK)
				.body(ApiResponse.success(messages));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetchFlights")
	public ResponseEntity<ApiResponse<List<FlightDto>>> getAllFlights() {
		List<FlightDto> flights = flightService.getAllFlights();
		return ResponseEntity.ok(ApiResponse.success(flights));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetchFlight/{id}")
	public ResponseEntity<ApiResponse<?>> getFlightById(@PathVariable Long id) {

		return flightService.getFlightById(id)
				.<ResponseEntity<ApiResponse<?>>>map(flight -> ResponseEntity.ok(ApiResponse.success(flight)))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.failure(ErrorCode.INVALID_REQUEST,
								List.of(new ApiError(ErrorCode.INVALID_REQUEST, "No flight exists with ID " + id)))));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteFlight/{id}")
	public ResponseEntity<ApiResponse<?>> deleteFlight(@PathVariable Long id) {

		boolean deleted = flightService.deleteFlight(id);

		if (deleted) {
			List<SuccessMessage> messages = List.of(new SuccessMessage("Flight soft deleted successfully"));
			return ResponseEntity.ok(ApiResponse.success(messages));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ErrorCode.INVALID_REQUEST,
				List.of(new ApiError(ErrorCode.INVALID_REQUEST, "No flight exists with ID " + id))));
	}

}
