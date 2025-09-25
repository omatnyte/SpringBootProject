package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import com.springboot.SpringBootProject.repository.UserSpringSecurityRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserSecurityService {

    @Autowired
    private UserSpringSecurityRepository userEntryRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveEntry(UserSpringSecurity entry) {
        userEntryRepository.save(entry);
    }
    public void saveNewEntry(UserSpringSecurity entry) {
        entry.setUserPassword(passwordEncoder.encode(entry.getUserPassword()));
        userEntryRepository.save(entry);
    }

    public List<UserSpringSecurity> getAllEntries() {
        return userEntryRepository.findAll();
    }

    public Optional<UserSpringSecurity> getEntryById(ObjectId id) {
        return userEntryRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id) {
        userEntryRepository.deleteById(id);
    }

    public UserSpringSecurity getUserByUsername(String userName) {
        return userEntryRepository.findByUserName(userName);
    }

    public void deleteEntryByUserName(UserSpringSecurity user) {
            userEntryRepository.delete(user);
    }
}
