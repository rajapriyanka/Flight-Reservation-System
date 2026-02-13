package com.flight.repository;

import com.flight.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByIsDeleted(String isDeleted);

	User findByUsernameAndIsDeleted(String username, String isDeleted);

	Optional<User> findByUsername(String username);

}
