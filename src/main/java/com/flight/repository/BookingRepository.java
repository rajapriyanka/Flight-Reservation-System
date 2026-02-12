package com.flight.repository;

import com.flight.entity.Booking;
import com.flight.entity.Trip;
import com.flight.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUser(User user);

	List<Booking> findByTripAndIsCancelled(Trip trip, String string);

}
