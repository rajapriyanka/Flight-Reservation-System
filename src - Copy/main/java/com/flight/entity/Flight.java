package com.flight.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight")
@Data
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "flight_no", nullable = false, unique = true, length = 20)
	private String flightNo;

	@Column(name = "flight_name", nullable = false, length = 100)
	private String flightName;

	@Column(name = "total_economy_seats", nullable = false)
	private int totalEconomySeats;

	@Column(name = "total_business_seats", nullable = false)
	private int totalBusinessSeats;

	@Column(name = "available_economy_seats", nullable = false)
	private int availableEconomySeats;

	@Column(name = "available_business_seats", nullable = false)
	private int availableBusinessSeats;

	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
	private List<Trip> route = new ArrayList<>();

	@Column(name = "is_deleted", length = 3, nullable = false)
	private String isDeleted = "NO";

	public Flight(String flightNo, String flightName) {
		this.flightNo = flightNo;
		this.flightName = flightName;
		this.totalEconomySeats = 0;
		this.totalBusinessSeats = 0;
		this.availableEconomySeats = 0;
		this.availableBusinessSeats = 0;
		this.route = new ArrayList<>();
		this.isDeleted = "NO";
	}

	public Flight() {

	}

	public Flight(Long id, String flightNo, String flightName, int totalEconomySeats, int totalBusinessSeats,
			int availableEconomySeats, int availableBusinessSeats, List<Trip> route, String isDeleted) {
		super();
		this.id = id;
		this.flightNo = flightNo;
		this.flightName = flightName;
		this.totalEconomySeats = totalEconomySeats;
		this.totalBusinessSeats = totalBusinessSeats;
		this.availableEconomySeats = availableEconomySeats;
		this.availableBusinessSeats = availableBusinessSeats;
		this.route = route;
		this.isDeleted = isDeleted;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public int getTotalEconomySeats() {
		return totalEconomySeats;
	}

	public void setTotalEconomySeats(int totalEconomySeats) {
		this.totalEconomySeats = totalEconomySeats;
	}

	public int getTotalBusinessSeats() {
		return totalBusinessSeats;
	}

	public void setTotalBusinessSeats(int totalBusinessSeats) {
		this.totalBusinessSeats = totalBusinessSeats;
	}

	public int getAvailableEconomySeats() {
		return availableEconomySeats;
	}

	public void setAvailableEconomySeats(int availableEconomySeats) {
		this.availableEconomySeats = availableEconomySeats;
	}

	public int getAvailableBusinessSeats() {
		return availableBusinessSeats;
	}

	public void setAvailableBusinessSeats(int availableBusinessSeats) {
		this.availableBusinessSeats = availableBusinessSeats;
	}

	public List<Trip> getRoute() {
		return route;
	}

	public void setRoute(List<Trip> route) {
		this.route = route;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

}
