package com.flight.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class FlightDto {

	private Long id;

	@NotBlank(message = "Flight number is required")
	@Size(max = 20, message = "Flight number cannot exceed 20 characters")
	private String flightNo;

	@NotBlank(message = "Flight name is required")
	@Size(max = 100, message = "Flight name cannot exceed 100 characters")
	private String flightName;

	@PositiveOrZero
	private int totalEconomySeats;

	@PositiveOrZero
	private int totalBusinessSeats;

	@PositiveOrZero
	private int availableEconomySeats;

	@PositiveOrZero
	private int availableBusinessSeats;
	
	@Column(name = "is_deleted", length = 3, nullable = false)
	private String isDeleted = "NO";

	public FlightDto() {
	}

	public FlightDto(Long id, String flightNo, String flightName, int totalEconomySeats, int totalBusinessSeats,
			int availableEconomySeats, int availableBusinessSeats) {

		this.id = id;
		this.flightNo = flightNo;
		this.flightName = flightName;
		this.totalEconomySeats = totalEconomySeats;
		this.totalBusinessSeats = totalBusinessSeats;
		this.availableEconomySeats = availableEconomySeats;
		this.availableBusinessSeats = availableBusinessSeats;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
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

}
