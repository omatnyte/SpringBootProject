package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository;

    public void saveEntry(User entry) {
        userEntryRepository.save(entry);
    }

    public List<User> getAllEntries() {
        return userEntryRepository.findAll();
    }

    public Optional<User> getEntryById(ObjectId id) {
        return userEntryRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id) {
        userEntryRepository.deleteById(id);
    }

    public User getUserByUsername(String userName) {
        return userEntryRepository.findByUserName(userName);
    }

    public void deleteEntryByUserName(User user) {

            userEntryRepository.delete(user);

    }
}
