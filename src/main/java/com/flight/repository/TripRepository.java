package com.flight.repository;

import com.flight.entity.Trip;
import com.flight.util.TripStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {
	List<Trip> findByFlight_Id(Long flightId);

	Optional<Trip> findByTripIdAndStatus(Long tripId, TripStatus tripStatus);

	@Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Trip t " + "WHERE t.flight.id = :flightId "
			+ "AND (:departureTime BETWEEN t.departureTime AND t.arrivalTime "
			+ "     OR :arrivalTime BETWEEN t.departureTime AND t.arrivalTime "
			+ "     OR t.departureTime BETWEEN :departureTime AND :arrivalTime)")
	boolean existsOverlappingTrip(@Param("flightId") Long flightId, @Param("departureTime") ZonedDateTime departureTime,
			@Param("arrivalTime") ZonedDateTime arrivalTime);

}
