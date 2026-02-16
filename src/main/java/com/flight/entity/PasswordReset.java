package com.flight.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

import com.flight.util.OtpType;

@Entity
@Table(name = "password_reset_otp")
public class PasswordReset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String otp;

	private ZonedDateTime expiryTime;

	@Column(length = 1)
	private char used;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OtpType type;

	public ZonedDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(ZonedDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public char getUsed() {
		return used;
	}

	public void setUsed(char used) {
		this.used = used;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OtpType getType() {
		return type;
	}

	public void setType(OtpType type) {
		this.type = type;
	}

}
