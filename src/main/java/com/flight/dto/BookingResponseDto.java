package com.flight.dto;

import com.flight.util.SeatClass;
import java.time.ZonedDateTime;

public class BookingResponseDto {

	private Long bookingId;

	private String username;
	private Long tripId;
	private Long flightId;

	private SeatClass seatClass;
	private int ticketCount;
	private double totalFare;

	private ZonedDateTime bookingTime;
	private boolean isCancelled;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public SeatClass getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	public double getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}

	public ZonedDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(ZonedDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public BookingResponseDto(Long bookingId, String username, Long tripId, Long flightId, SeatClass seatClass,
			int ticketCount, double totalFare, ZonedDateTime bookingTime, boolean isCancelled) {
		super();
		this.bookingId = bookingId;
		this.username = username;
		this.tripId = tripId;
		this.flightId = flightId;
		this.seatClass = seatClass;
		this.ticketCount = ticketCount;
		this.totalFare = totalFare;
		this.bookingTime = bookingTime;
		this.isCancelled = isCancelled;
	}

}
