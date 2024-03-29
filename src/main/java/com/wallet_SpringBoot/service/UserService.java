package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Transactional
    public User saveUser (User user){
        return repository.save(user);
    }

    @Transactional
    public User updateUser (User user){
        User existingUser = repository.findById(user.getId()).orElse(null);
        existingUser.setLogin(user.getLogin());
        existingUser.setPassword(user.getPassword());
        return repository.save(existingUser);
    }

    @Transactional
    public void deleteUserById (long id){
        repository.deleteById(id);
    }

    @Transactional
    public User findUserById (long id){
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public String findPasswordByLogin(String login) {
        User user = repository.findByLogin(login);
        return (user != null) ? user.getPassword() : null;
    }

    @Transactional
    public Long findIdByLogin (String login){
        User user = repository.findByLogin(login);
        return user.getId();
    }
    public boolean isUserExistsByLogin(String login) {
        return repository.findByLogin(login) != null;
    }
}
