package com.flight.service;

import com.flight.dto.UserDto;
import com.flight.entity.User;
import com.flight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private UserDto toDTO(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getPhone(), user.getWallet());
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
		user.setPassword(dto.getPassword());

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
		if (userOpt.isEmpty()) {
			return false;
		}
		User user = userOpt.get();
		if ("YES".equalsIgnoreCase(user.getIsDeleted())) {
			return false;
		}
		user.setIsDeleted("YES");
		userRepository.save(user);
		return true;
	}

}
