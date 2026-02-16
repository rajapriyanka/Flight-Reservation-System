package com.flight.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.flight.dto.AuthRequestDto;
import com.flight.dto.ForgotPasswordRequestDto;
import com.flight.service.UserService;
import com.flight.util.ApiError;
import com.flight.util.ApiResponse;
import com.flight.util.ErrorCode;
import com.flight.util.JwtUtil;
import com.flight.util.SuccessMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			String token = jwtUtil.generateToken(request.getUsername());
			return ResponseEntity.ok("You are authenticated successfully.\nToken: " + token);

		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401).body("Invalid username or password");
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {

		try {
			String result = userService.handleForgotPassword(request);
			List<SuccessMessage> messages = List.of(new SuccessMessage(result));
			return ResponseEntity.ok(ApiResponse.success(messages));

		} catch (IllegalArgumentException ex) {
			ApiError apiError = new ApiError(ErrorCode.INVALID_REQUEST, ex.getMessage());
			return ResponseEntity.ok().body(ApiResponse.failure(apiError.getResponseCode(), List.of(apiError)));

		} catch (RuntimeException ex) {
			ApiError apiError = new ApiError(ErrorCode.USER_NOT_FOUND, ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ApiResponse.failure(apiError.getResponseCode(), List.of(apiError)));
		}
	}

}
