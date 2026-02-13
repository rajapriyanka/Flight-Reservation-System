package com.flight.repository;

import com.flight.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

	Optional<Flight> findByIdAndIsDeleted(Long id, String isDeleted);

	Optional<Flight> findByFlightNoAndIsDeleted(String flightNo, String isDeleted);

	List<Flight> findAllByIsDeleted(String isDeleted);

	boolean existsByFlightNoAndIsDeleted(String flightNo, String isDeleted);
}
