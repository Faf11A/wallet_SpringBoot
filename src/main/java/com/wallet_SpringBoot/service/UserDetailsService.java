package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.UserDetails;
import com.wallet_SpringBoot.repository.UserDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository repository;

    @Transactional
    public UserDetails saveUserDetails(UserDetails userDetails) {
        return repository.save(userDetails);
    }

    @Transactional
    public void deleteUserDetails(Long userDetailsId) {
        repository.deleteById(userDetailsId);
    }

    @Transactional
    public UserDetails updateUserDetails(UserDetails updatedUserDetails) {
        UserDetails userDetailsFromDB = repository.findById(updatedUserDetails.getId()).orElse(null);

        if (userDetailsFromDB != null) {
            userDetailsFromDB.setFirstName(updatedUserDetails.getFirstName());
            userDetailsFromDB.setLastName(updatedUserDetails.getLastName());
            userDetailsFromDB.setEmail(updatedUserDetails.getEmail());
            userDetailsFromDB.setBirthDate(updatedUserDetails.getBirthDate());

            userDetailsFromDB = repository.save(userDetailsFromDB);

            return userDetailsFromDB;
        } else {
            return null;
        }
    }

    @Transactional
    public UserDetails getUserDetailsById(Long userDetailsId) {
        return repository.findById(userDetailsId).orElse(null);
    }

    @Transactional
    public String findNameById(Long userId) {
        UserDetails userDetails = repository.findById(userId).orElse(null);
        if (userDetails != null) {
            return userDetails.getFirstName();
        } else {
            return null;
        }
    }

    @Transactional
    public Optional<UserDetails> findUserDetailsByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
