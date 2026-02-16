package com.flight.controller;

import com.flight.dto.ChangePasswordRequestDto;
import com.flight.dto.UserDto;
import com.flight.service.UserService;
import com.flight.util.ApiError;
import com.flight.util.ApiResponse;
import com.flight.util.ErrorCode;
import com.flight.util.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/change-password")
	public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {

		try {
			String result = userService.handleChangePassword(request);
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

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/addUser")
	public ResponseEntity<ApiResponse<List<SuccessMessage>>> saveUser(@Valid @RequestBody UserDto userDTO) {

		userService.saveUser(userDTO);

		boolean isCreate = userDTO.getId() == null;

		List<SuccessMessage> messages = List
				.of(new SuccessMessage(isCreate ? "User created successfully" : "User updated successfully"));

		return ResponseEntity.status(isCreate ? HttpStatus.OK : HttpStatus.OK).body(ApiResponse.success(messages));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetchUser")
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
		List<UserDto> users = userService.getAllUsers();
		return ResponseEntity.ok(ApiResponse.success(users));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long id) {

		boolean deleted = userService.deleteUser(id);

		if (deleted) {
			List<SuccessMessage> messages = List.of(new SuccessMessage("User soft deleted successfully"));
			return ResponseEntity.ok(ApiResponse.success(messages));
		}

		ApiError apiError = new ApiError(ErrorCode.USER_NOT_FOUND, "No user exists with ID " + id);

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.failure(apiError.getResponseCode(), List.of(apiError)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetchUser/{id}")
	public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Long id) {

		return userService.getUserById(id)
				.<ResponseEntity<ApiResponse<?>>>map(user -> ResponseEntity.ok(ApiResponse.success(user)))
				.orElseGet(() -> {

					ApiError apiError = new ApiError(ErrorCode.USER_NOT_FOUND, "No user exists with ID " + id);

					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(ApiResponse.failure(apiError.getResponseCode(), List.of(apiError)));
				});
	}

}
