package com.wallet_SpringBoot.repository;

import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUserId(Long userId);
}


