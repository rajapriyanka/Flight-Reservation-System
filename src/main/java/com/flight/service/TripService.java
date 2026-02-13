package com.flight.service;

import com.flight.dto.TripDto;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	private TripDto toDto(Trip trip) {
		return new TripDto(trip.getTripId(), trip.getFlight().getId(), trip.getFrom(), trip.getTo(),
				trip.getDepartureTime(), trip.getArrivalTime(), trip.getEconomyFare(), trip.getBusinessFare(),
				trip.getStatus(), trip.getActualDepartureTime(), trip.getActualArrivalTime());
	}

	public TripDto createOrUpdateTrip(TripDto dto) {

	    Trip trip;
	    if (dto.getTripId() != null) {
	        Optional<Trip> tripOpt = tripRepository.findById(dto.getTripId());
	        if (tripOpt.isEmpty()) {
	            return null;
	        }
	        trip = tripOpt.get();
	    } else {
	        trip = new Trip();
	    }

	    Flight flight = flightRepository.findById(dto.getFlightId()).orElse(null);
	    if (flight == null) {
	        return null;
	    }

	    boolean overlapExists = tripRepository.existsOverlappingTrip(
	            flight.getId(),
	            dto.getDepartureTime(),
	            dto.getArrivalTime()
	    );

	    if (overlapExists && (dto.getTripId() == null || !trip.getTripId().equals(dto.getTripId()))) {
	        throw new IllegalArgumentException("A trip for this flight already exists in the given time range");
	    }

	    trip.setFlight(flight);
	    trip.setFrom(dto.getFrom());
	    trip.setTo(dto.getTo());
	    trip.setDepartureTime(dto.getDepartureTime());
	    trip.setArrivalTime(dto.getArrivalTime());
	    trip.setEconomyFare(dto.getEconomyFare());
	    trip.setBusinessFare(dto.getBusinessFare());
	    trip.setStatus(dto.getStatus());

	    return toDto(tripRepository.save(trip));
	}

	public List<TripDto> getAllTrips() {
		return tripRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	public Optional<TripDto> getTripById(Long id) {
		return tripRepository.findById(id).map(this::toDto);
	}

	public boolean deleteTrip(Long id) {
		Optional<Trip> tripOpt = tripRepository.findById(id);
		if (tripOpt.isPresent()) {
			tripRepository.delete(tripOpt.get());
			return true;
		}
		return false;
	}

	@Transactional
	public void startTrip(Long tripId) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (trip.getStatus() != TripStatus.YET_TO_DEPART) {
			throw new IllegalStateException("Trip cannot be started. Current status: " + trip.getStatus());
		}

		trip.setActualDepartureTime(ZonedDateTime.now());
		trip.setStatus(TripStatus.TRAVELLING);
		tripRepository.save(trip);

		System.out.println("Trip started at " + trip.getActualDepartureTime());
	}

	@Transactional
	public void endTrip(Long tripId) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (trip.getStatus() != TripStatus.TRAVELLING) {
			throw new IllegalStateException("Trip is not ongoing. Current status: " + trip.getStatus());
		}

		trip.setActualArrivalTime(ZonedDateTime.now());
		trip.setStatus(TripStatus.COMPLETED);
		tripRepository.save(trip);

		restoreSeatsByTrip(tripId);

		System.out.println("Trip ended at " + trip.getActualArrivalTime());
	}

	public void restoreSeatsByTrip(Long tripId) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		List<Booking> bookings = bookingRepository.findByTripAndIsCancelled(trip, "NO");

		int ecoSeats = 0;
		int busSeats = 0;
		Flight flight = null;

		for (Booking booking : bookings) {
			flight = booking.getFlight();
			if (booking.getSeatClass() == SeatClass.ECONOMY) {
				ecoSeats += booking.getTicketCount();
			} else {
				busSeats += booking.getTicketCount();
			}
		}

		if (flight != null) {
			flight.setAvailableEconomySeats(flight.getAvailableEconomySeats() + ecoSeats);
			flight.setAvailableBusinessSeats(flight.getAvailableBusinessSeats() + busSeats);
			flightRepository.save(flight);

			System.out.println("Restored " + ecoSeats + " economy and " + busSeats + " business seats for flight "
					+ flight.getFlightNo());
		}
	}

	@Transactional
	public void cancelBookingsByTrip(Long tripId) {

		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (trip.getStatus() != TripStatus.YET_TO_DEPART)
			throw new IllegalStateException("Only trips with status YET_TO_DEPART can be cancelled");

		List<Booking> bookings = bookingRepository.findByTripAndIsCancelled(trip, "NO");

		for (Booking booking : bookings) {

			User user = booking.getUser();
			user.setWallet(user.getWallet() + (float) booking.getTotalFare());
			userRepository.save(user);

			Flight flight = booking.getFlight();
			if (booking.getSeatClass() == SeatClass.ECONOMY)
				flight.setAvailableEconomySeats(flight.getAvailableEconomySeats() + booking.getTicketCount());
			else
				flight.setAvailableBusinessSeats(flight.getAvailableBusinessSeats() + booking.getTicketCount());
			flightRepository.save(flight);

			booking.setIsCancelled("YES");
			booking.setCancellationTime(ZonedDateTime.now());
			bookingRepository.save(booking);
		}
	}

	@Transactional
	public void cancelTrip(Long tripId) {
		cancelBookingsByTrip(tripId);
		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
		trip.setStatus(TripStatus.CANCELLED);
		tripRepository.save(trip);
	}
}
