package com.flight.service;

import com.flight.dto.ChangePasswordRequestDto;
import com.flight.dto.ForgotPasswordRequestDto;
import com.flight.dto.UserDto;
import com.flight.entity.PasswordReset;
import com.flight.entity.User;
import com.flight.repository.PasswordResetRepository;
import com.flight.repository.UserRepository;
import com.flight.util.OtpType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetRepository otpRepository;
	private final EmailService emailService;

	public UserService(UserRepository userRepository, PasswordResetRepository otpRepository,
			PasswordEncoder passwordEncoder, EmailService emailService) {
		this.userRepository = userRepository;
		this.otpRepository = otpRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	private UserDto toDTO(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPhone(), user.getWallet(), user.getRole());
	}

	public String handleChangePassword(ChangePasswordRequestDto changePasswordRequestDto) {
		User user = getUserByUsername(changePasswordRequestDto.getUsername());

		if (isOtpRequest(changePasswordRequestDto.getOldPassword(), changePasswordRequestDto.getNewPassword(),
				changePasswordRequestDto.getOtp())) {
			sendOtpToUser(user, "Reset Password OTP - Flight Reservation System", OtpType.CHANGE_PASSWORD);
			return "OTP sent to registered email";
		}

		if (isChangePasswordRequest(changePasswordRequestDto.getOldPassword(),
				changePasswordRequestDto.getNewPassword(), changePasswordRequestDto.getOtp())) {

			if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
				throw new IllegalArgumentException("Old password is incorrect");
			}

			if (!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmPassword())) {
				throw new IllegalArgumentException("New password and confirm password do not match");
			}

			if (passwordEncoder.matches(changePasswordRequestDto.getNewPassword(), user.getPassword())) {
				throw new IllegalArgumentException("New password cannot be same as old password");
			}

			validateOtp(user, changePasswordRequestDto.getOtp(), OtpType.CHANGE_PASSWORD);

			updatePassword(user, changePasswordRequestDto.getNewPassword());
			return "Password changed successfully";
		}

		throw new IllegalArgumentException("Invalid request format");
	}

	public String handleForgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
		User user = getUserByUsername(forgotPasswordRequestDto.getUsername());

		if (forgotPasswordRequestDto.getOtp() == null && forgotPasswordRequestDto.getNewPassword() == null) {
			sendOtpToUser(user, "Forgot Password OTP - Flight Reservation System", OtpType.FORGOT_PASSWORD);
			return "OTP sent to registered email";
		}

		if (forgotPasswordRequestDto.getOtp() != null && forgotPasswordRequestDto.getNewPassword() != null) {

			if (!forgotPasswordRequestDto.getNewPassword().equals(forgotPasswordRequestDto.getConfirmPassword())) {
				throw new IllegalArgumentException("New password and confirm password do not match");
			}

			if (passwordEncoder.matches(forgotPasswordRequestDto.getNewPassword(), user.getPassword())) {
				throw new IllegalArgumentException("New password cannot be same as old password");
			}

			validateOtp(user, forgotPasswordRequestDto.getOtp(), OtpType.FORGOT_PASSWORD);

			updatePassword(user, forgotPasswordRequestDto.getNewPassword());
			return "Password reset successful";
		}

		throw new IllegalArgumentException("Invalid request format");
	}

	private String generateOtp() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}

	private User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid username"));
	}

	private void sendOtpToUser(User user, String subject, OtpType type) {
		String otpValue = generateOtp();

		PasswordReset otp = new PasswordReset();
		otp.setUser(user);
		otp.setOtp(otpValue);
		otp.setExpiryTime(ZonedDateTime.now().plusMinutes(5));
		otp.setUsed('N');
		otp.setType(type);

		otpRepository.save(otp);

		emailService.sendOtp(user.getEmail(), user.getFirstName(), otpValue, subject);
	}

	private void validateOtp(User user, String otpValue, OtpType otpType) {
		PasswordReset otpRecord = otpRepository
				.findTopByUserIdAndUsedAndTypeOrderByExpiryTimeDesc(user.getId(), 'N', otpType)
				.orElseThrow(() -> new RuntimeException("No valid OTP found for " + otpType));

		if (!otpRecord.getOtp().equals(otpValue)) {
			throw new RuntimeException("Invalid OTP");
		}

		if (otpRecord.getExpiryTime().isBefore(ZonedDateTime.now())) {
			throw new RuntimeException("OTP has expired");
		}

		otpRecord.setUsed('Y');
		otpRepository.save(otpRecord);
	}

	private void updatePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	private boolean isOtpRequest(String oldPass, String newPass, String otp) {
		return oldPass == null && newPass == null && otp == null;
	}

	private boolean isChangePasswordRequest(String oldPass, String newPass, String otp) {
		return oldPass != null && newPass != null && otp != null;
	}

	public UserDto saveUser(UserDto dto) {
		User user;

		if (dto.getId() != null) {
			user = userRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("User not found"));
		} else {
			user = new User();
			user.setIsDeleted("NO");
		}

		user.setUsername(dto.getUsername());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPhone(dto.getPhone());
		user.setWallet(dto.getWallet());
		user.setRole(dto.getRole());

		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		User savedUser = userRepository.save(user);
		return toDTO(savedUser);
	}

	public Optional<UserDto> getUserById(Long id) {
		return userRepository.findById(id).filter(user -> "NO".equals(user.getIsDeleted())).map(this::toDTO);
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findByIsDeleted("NO").stream().map(this::toDTO).collect(Collectors.toList());
	}

	public boolean deleteUser(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isEmpty())
			return false;

		User user = userOpt.get();
		if ("YES".equalsIgnoreCase(user.getIsDeleted()))
			return false;

		user.setIsDeleted("YES");
		userRepository.save(user);
		return true;
	}
}
