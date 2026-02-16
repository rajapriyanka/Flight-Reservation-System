package com.flight.dto;

import com.flight.util.Role;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class UserDto {

	private Long id;

	@NotBlank(message = "Username is required")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
	private String password;
	@NotBlank(message = "First Name is required")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstName;

	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	@Size(max = 100, message = "Email cannot exceed 100 characters")
	private String email;

	@Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
	private String phone;

	@NotNull(message = "Wallet is required")
	@PositiveOrZero(message = "Wallet must be zero or positive")
	private Float wallet;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	public UserDto(Long id, String username, String firstName, String lastName, String email, String phone,
			Float wallet, Role role) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.wallet = wallet;
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Float getWallet() {
		return wallet;
	}

	public void setWallet(Float wallet) {
		this.wallet = wallet;
	}

//	public String getIsDeleted() {
//		return isDeleted;
//	}
//
//	public void setIsDeleted(String isDeleted) {
//		this.isDeleted = isDeleted;
//	}
}
