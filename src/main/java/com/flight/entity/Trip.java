package com.flight.entity;

import com.flight.util.TripStatus;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "trip")
public class Trip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tripId;

	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight flight;

	@Column(name = "from_city", nullable = false, length = 50)
	private String from;

	@Column(name = "to_city", nullable = false, length = 50)
	private String to;

	@Column(name = "departure_time", nullable = false)
	private ZonedDateTime departureTime;

	@Column(name = "arrival_time", nullable = false)
	private ZonedDateTime arrivalTime;

	@Column(name = "economy_fare", nullable = false)
	private double economyFare;

	@Column(name = "business_fare", nullable = false)
	private double businessFare;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private TripStatus status = TripStatus.YET_TO_DEPART;

	@Column(name = "actual_departure_time")
	private ZonedDateTime actualDepartureTime;

	@Column(name = "actual_arrival_time")
	private ZonedDateTime actualArrivalTime;

	public Trip(String from, String to, ZonedDateTime departureTime, ZonedDateTime arrivalTime, double economyFare,
			double businessFare) {
		this.from = from;
		this.to = to;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.economyFare = economyFare;
		this.businessFare = businessFare;
		this.status = TripStatus.YET_TO_DEPART;
	}

	public Trip() {

	}

	public Trip(Flight flight, String from, String to, ZonedDateTime departureTime, ZonedDateTime arrivalTime,
			double economyFare, double businessFare) {
		this.flight = flight;
		this.from = from;
		this.to = to;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.economyFare = economyFare;
		this.businessFare = businessFare;
		this.status = TripStatus.YET_TO_DEPART;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
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
