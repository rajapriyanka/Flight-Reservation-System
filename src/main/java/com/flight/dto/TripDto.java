package com.flight.dto;

import com.flight.util.TripStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public class TripDto {
	private Long tripId;
	@NotNull(message = "Flight ID is required")
	private Long flightId;

	@NotNull(message = "Source location is required")
	@Size(min = 2, max = 100, message = "Source must be between 2 and 100 characters")
	private String from;

	@NotNull(message = "Destination location is required")
	@Size(min = 2, max = 100, message = "Destination must be between 2 and 100 characters")
	private String to;

	@NotNull(message = "Departure time is required")
	@Future(message = "Departure time must be in the future")
	private ZonedDateTime departureTime;

	@NotNull(message = "Arrival time is required")
	@Future(message = "Arrival time must be in the future")
	private ZonedDateTime arrivalTime;

	@Min(value = 0, message = "Economy fare must be zero or positive")
	private double economyFare;

	@Min(value = 0, message = "Business fare must be zero or positive")
	private double businessFare;

	@NotNull(message = "Trip status is required")
	private TripStatus status;

	private ZonedDateTime actualDepartureTime;
	private ZonedDateTime actualArrivalTime;

	public TripDto(Long tripId, @NotNull(message = "Flight ID is required") Long flightId, String from, String to,
			ZonedDateTime departureTime, ZonedDateTime arrivalTime, double economyFare, double businessFare,
			TripStatus status, ZonedDateTime actualDepartureTime, ZonedDateTime actualArrivalTime) {
		super();
		this.tripId = tripId;
		this.flightId = flightId;
		this.from = from;
		this.to = to;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.economyFare = economyFare;
		this.businessFare = businessFare;
		this.status = status;
		this.actualDepartureTime = actualDepartureTime;
		this.actualArrivalTime = actualArrivalTime;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public ZonedDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(ZonedDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public ZonedDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(ZonedDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getEconomyFare() {
		return economyFare;
	}

	public void setEconomyFare(double economyFare) {
		this.economyFare = economyFare;
	}

	public double getBusinessFare() {
		return businessFare;
	}

	public void setBusinessFare(double businessFare) {
		this.businessFare = businessFare;
	}

	public TripStatus getStatus() {
		return status;
	}

	public void setStatus(TripStatus status) {
		this.status = status;
	}

	public ZonedDateTime getActualDepartureTime() {
		return actualDepartureTime;
	}

	public void setActualDepartureTime(ZonedDateTime actualDepartureTime) {
		this.actualDepartureTime = actualDepartureTime;
	}

	public ZonedDateTime getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(ZonedDateTime actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

}
