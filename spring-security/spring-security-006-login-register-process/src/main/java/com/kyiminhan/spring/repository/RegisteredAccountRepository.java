package com.kyiminhan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyiminhan.spring.entity.RegisteredAccount;

@Repository
public interface RegisteredAccountRepository extends JpaRepository<RegisteredAccount, Long> {

	Optional<RegisteredAccount> findByEmail(String email);

}