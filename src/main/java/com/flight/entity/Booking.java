package com.flight.entity;

import com.flight.util.SeatClass;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "booking")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight flight;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "trip_id", nullable = false)
	private Trip trip;

	@Enumerated(EnumType.STRING)
	@Column(name = "seat_class", nullable = false, length = 20)
	private SeatClass seatClass;

	@Column(name = "ticket_count", nullable = false)
	private int ticketCount;

	@Column(name = "total_fare", nullable = false)
	private double totalFare;

	@Column(name = "booking_time", nullable = false)
	private ZonedDateTime bookingTime;

	@Column(name = "cancellation_time")
	private ZonedDateTime cancellationTime;

	@Column(name = "is_cancelled", length = 3, nullable = false)
	private String isCancelled = "NO";

	public Booking(Flight flight, User user, Trip trip, SeatClass seatClass, int ticketCount, double totalFare,
			ZonedDateTime bookingTime) {
		this.flight = flight;
		this.user = user;
		this.trip = trip;
		this.seatClass = seatClass;
		this.ticketCount = ticketCount;
		this.totalFare = totalFare;
		this.bookingTime = bookingTime;
		this.cancellationTime = null;
		this.isCancelled = "NO";
	}

	public Booking() {

	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
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

	public ZonedDateTime getCancellationTime() {
		return cancellationTime;
	}

	public void setCancellationTime(ZonedDateTime cancellationTime) {
		this.cancellationTime = cancellationTime;
	}

	@Transient
	public boolean isBookingCancelled() {
		return "YES".equalsIgnoreCase(this.isCancelled);
	}

	public String getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(String isCancelled) {
		this.isCancelled = isCancelled;
	}

}
