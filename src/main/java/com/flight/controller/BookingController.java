package com.flight.controller;

import com.flight.dto.BookingRequestDto;
import com.flight.dto.BookingResponseDto;
import com.flight.dto.CancelBookingRequestDto;
import com.flight.service.BookingService;
import com.flight.util.ApiResponse;
import com.flight.util.SuccessMessage;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	
	@GetMapping("/fetchBooking/{username}")
	public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getBookingsByUser(@PathVariable String name) {
		List<BookingResponseDto> bookings = bookingService.getBookingsByUsername(username);
		return ResponseEntity.ok(ApiResponse.success(bookings));
	}

	@GetMapping("/fetchBooking/{username}")
	public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getBookingsByUser(@PathVariable String username) {
		List<BookingResponseDto> bookings = bookingService.getBookingsByUsername(username);
		return ResponseEntity.ok(ApiResponse.success(bookings));
	}

	@PostMapping("/book")
	public ResponseEntity<ApiResponse<List<SuccessMessage>>> bookTicket(@Valid @RequestBody BookingRequestDto request) {
		bookingService.bookTicket(request.getUsername(), request.getTripId(), request.getSeatClass(),
				request.getTicketCount());

		List<SuccessMessage> messages = List.of(new SuccessMessage("Booking completed successfully"));
		return ResponseEntity.ok(ApiResponse.success(messages));
	}

	@PostMapping("/cancel")
	public ResponseEntity<ApiResponse<List<SuccessMessage>>> cancelBooking(
			@Valid @RequestBody CancelBookingRequestDto request) {
		bookingService.cancelBookingById(request.getUsername(), request.getBookingId());

		List<SuccessMessage> messages = List.of(new SuccessMessage("Booking cancelled successfully"));
		return ResponseEntity.ok(ApiResponse.success(messages));
	}
}
