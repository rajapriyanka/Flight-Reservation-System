package com.flight.service;

import com.flight.dto.FlightDto;
import com.flight.entity.Flight;
import com.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

	@Autowired
	private FlightRepository flightRepository;

	private FlightDto toDto(Flight flight) {
		return new FlightDto(flight.getId(), flight.getFlightNo(), flight.getFlightName(),
				flight.getTotalEconomySeats(), flight.getTotalBusinessSeats(), flight.getAvailableEconomySeats(),
				flight.getAvailableBusinessSeats());
	}

	public FlightDto createOrUpdateFlight(FlightDto dto) {

		Flight flight;
		if (dto.getId() != null) {
			flight = flightRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Flight not found"));
		} else {
			if (flightRepository.findByFlightNoAndIsDeleted(dto.getFlightNo(), "NO").isPresent()) {
				throw new RuntimeException("Flight number already exists");
			}
			flight = new Flight();
			flight.setIsDeleted("NO");
		}

		flight.setFlightNo(dto.getFlightNo());
		flight.setFlightName(dto.getFlightName());
		flight.setTotalEconomySeats(dto.getTotalEconomySeats());
		flight.setTotalBusinessSeats(dto.getTotalBusinessSeats());
		flight.setAvailableEconomySeats(dto.getAvailableEconomySeats());
		flight.setAvailableBusinessSeats(dto.getAvailableBusinessSeats());

		return toDto(flightRepository.save(flight));
	}

	public List<FlightDto> getAllFlights() {
		return flightRepository.findAllByIsDeleted("NO").stream().map(this::toDto).collect(Collectors.toList());
	}

	public Optional<FlightDto> getFlightById(Long id) {
		return flightRepository.findByIdAndIsDeleted(id, "NO").map(this::toDto);
	}

	public boolean deleteFlight(Long id) {
		Optional<Flight> flightOpt = flightRepository.findByIdAndIsDeleted(id, "NO");

		if (flightOpt.isPresent()) {
			Flight flight = flightOpt.get();
			flight.setIsDeleted("YES");
			flightRepository.save(flight);
			return true;
		}
		return false;
	}
}
