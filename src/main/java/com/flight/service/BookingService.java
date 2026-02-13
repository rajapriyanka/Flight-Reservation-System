package com.flight.service;

import com.flight.dto.BookingResponseDto;
import com.flight.entity.Booking;
import com.flight.entity.Flight;
import com.flight.entity.Trip;
import com.flight.entity.User;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import com.flight.repository.TripRepository;
import com.flight.repository.UserRepository;
import com.flight.util.SeatClass;
import com.flight.util.TripStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.ZonedDateTime;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private FlightRepository flightRepository;

	private BookingResponseDto toDto(Booking booking) {
		return new BookingResponseDto(booking.getBookingId(), booking.getUser().getUsername(),
				booking.getTrip().getTripId(), booking.getFlight().getId(), booking.getSeatClass(),
				booking.getTicketCount(), booking.getTotalFare(), booking.getBookingTime(),
				booking.isBookingCancelled());
	}

	public List<BookingResponseDto> getAllBookings() {
		return bookingRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<BookingResponseDto> getBookingsByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		return bookingRepository.findByUser(user).stream().map(this::toDto).collect(Collectors.toList());
	}

	@Transactional
	public BookingResponseDto bookTicket(String username, Long tripId, String seatClassStr, int ticketCount) {

		if (ticketCount <= 0)
			throw new IllegalArgumentException("Ticket count must be greater than 0");

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		TripStatus tripStatus = TripStatus.YET_TO_DEPART;

		Trip trip = tripRepository.findByTripIdAndStatus(tripId, tripStatus)
				.orElseThrow(() -> new IllegalArgumentException("Trip not available"));

		Flight flight = trip.getFlight();

		SeatClass seatClass;
		try {
			seatClass = SeatClass.valueOf(seatClassStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid seat class");
		}

		double fare = seatClass == SeatClass.ECONOMY ? trip.getEconomyFare() : trip.getBusinessFare();
		double totalFare = fare * ticketCount;

		if (seatClass == SeatClass.ECONOMY) {
			if (flight.getAvailableEconomySeats() < ticketCount)
				throw new IllegalStateException("Not enough economy seats");
		} else {
			if (flight.getAvailableBusinessSeats() < ticketCount)
				throw new IllegalStateException("Not enough business seats");
		}

		if (user.getWallet() < totalFare)
			throw new IllegalStateException("Insufficient wallet balance");

		user.setWallet(user.getWallet() - (float) totalFare);

		if (seatClass == SeatClass.ECONOMY) {
			flight.setAvailableEconomySeats(flight.getAvailableEconomySeats() - ticketCount);
		} else {
			flight.setAvailableBusinessSeats(flight.getAvailableBusinessSeats() - ticketCount);
		}

		Booking booking = new Booking(flight, user, trip, seatClass, ticketCount, totalFare, ZonedDateTime.now());
		bookingRepository.save(booking);

		userRepository.save(user);
		flightRepository.save(flight);

		return new BookingResponseDto(booking.getBookingId(), user.getUsername(), trip.getTripId(), flight.getId(),
				seatClass, ticketCount, totalFare, booking.getBookingTime(), booking.isBookingCancelled());
	}

	@Transactional
	public void cancelBookingById(String username, Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new IllegalArgumentException("Booking not found"));

		User user = booking.getUser();

		if (!user.getUsername().equals(username)) {
			throw new IllegalArgumentException("Booking does not belong to this user");
		}

		if (booking.isBookingCancelled()) {
			throw new IllegalStateException("Booking already cancelled");
		}

		Trip trip = booking.getTrip();
		if (trip.getStatus() != TripStatus.YET_TO_DEPART) {
			throw new IllegalStateException("Cancellation not allowed. Trip status is: " + trip.getStatus());
		}

		double totalFare = booking.getTotalFare();
		user.setWallet(user.getWallet() + (float) totalFare);
		userRepository.save(user);

		Flight flight = trip.getFlight();
		if (booking.getSeatClass() == SeatClass.ECONOMY) {
			flight.setAvailableEconomySeats(flight.getAvailableEconomySeats() + booking.getTicketCount());
		} else {
			flight.setAvailableBusinessSeats(flight.getAvailableBusinessSeats() + booking.getTicketCount());
		}
		flightRepository.save(flight);

		booking.setIsCancelled("YES");
		booking.setCancellationTime(ZonedDateTime.now());
		bookingRepository.save(booking);
	}
}
