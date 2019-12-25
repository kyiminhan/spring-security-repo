package com.kyiminhan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyiminhan.spring.entity.PasswordRequest;

@Repository
public interface PasswordRequestRepository extends JpaRepository<PasswordRequest, Long> {

	Optional<PasswordRequest> findByUuid(String uuid);

	void deleteAllByEmail(String email);

}