package com.wallet_SpringBoot.repository;

import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}


