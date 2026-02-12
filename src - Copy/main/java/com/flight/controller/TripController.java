package com.flight.controller;

import com.flight.dto.TripDto;
import com.flight.service.TripService;
import com.flight.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

	@Autowired
	private TripService tripService;

	@GetMapping("/fetchTrips")
	public ResponseEntity<ApiResponse<List<TripDto>>> getAllTrips() {
		return ResponseEntity.ok(ApiResponse.success(tripService.getAllTrips()));
	}

	@GetMapping("/fetchTrip/{id}")
	public ResponseEntity<ApiResponse<?>> getTripById(@PathVariable Long id) {

		return tripService.getTripById(id)
				.<ResponseEntity<ApiResponse<?>>>map(trip -> ResponseEntity.ok(ApiResponse.success(trip)))
				.orElseGet(() -> {
					ApiError error = new ApiError(ErrorCode.INVALID_REQUEST, "No trip exists with ID " + id);

					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(ApiResponse.failure(error.getResponseCode(), List.of(error)));
				});
	}

	@PostMapping("/startTrip/{id}")
	public ResponseEntity<ApiResponse<?>> startTrip(@PathVariable Long id) {
		tripService.startTrip(id);

		return ResponseEntity.ok(ApiResponse.success(List.of(new SuccessMessage("Trip started successfully"))));
	}

	@PostMapping("/endTrip/{id}")
	public ResponseEntity<ApiResponse<?>> endTrip(@PathVariable Long id) {
		tripService.endTrip(id);

		return ResponseEntity.ok(ApiResponse.success(List.of(new SuccessMessage("Trip ended successfully"))));
	}

	@PostMapping("/addTrip")
	public ResponseEntity<ApiResponse<?>> addTrip(@Valid @RequestBody TripDto tripDto) {

		TripDto saved = tripService.createOrUpdateTrip(tripDto);

		if (saved == null) {
			ApiError error = new ApiError(ErrorCode.INVALID_REQUEST, "Invalid flight or trip ID");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.failure(error.getResponseCode(), List.of(error)));
		}

		String msg = tripDto.getTripId() == null ? "Trip created successfully" : "Trip updated successfully";

		return ResponseEntity.status(tripDto.getTripId() == null ? HttpStatus.CREATED : HttpStatus.OK)
				.body(ApiResponse.success(List.of(new SuccessMessage(msg))));
	}

	@DeleteMapping("/deleteTrip/{id}")
	public ResponseEntity<ApiResponse<?>> deleteTrip(@PathVariable Long id) {

		if (tripService.deleteTrip(id)) {
			return ResponseEntity.ok(ApiResponse.success(List.of(new SuccessMessage("Trip deleted successfully"))));
		}

		ApiError error = new ApiError(ErrorCode.INVALID_REQUEST, "No trip exists with ID " + id);

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.failure(error.getResponseCode(), List.of(error)));
	}

	@PostMapping("/cancelTrip/{id}")
	public ResponseEntity<ApiResponse<?>> cancelTrip(@PathVariable Long id) {

		tripService.cancelTrip(id);

		return ResponseEntity.ok(ApiResponse
				.success(List.of(new SuccessMessage("Trip and all associated bookings cancelled successfully"))));
	}

}
