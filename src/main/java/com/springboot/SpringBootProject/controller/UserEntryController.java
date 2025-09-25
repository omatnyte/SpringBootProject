package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.service.JournalEntryService;
import com.springboot.SpringBootProject.service.UserEntryService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserEntryController {

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping()
    public ResponseEntity<?> getAllEntries() {

        return new ResponseEntity<>(userEntryService.getAllEntries(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Optional<User> entry = userEntryService.getEntryById(id);

        if(entry.isPresent()){
            System.out.println("Hello");
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@Valid @RequestBody User entry) {

        try{
            userEntryService.saveEntry(entry);
            return new ResponseEntity<>("User entry added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        try {
            userEntryService.deleteEntryById(id);
            return new ResponseEntity<>("User entry deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEntryByUserName(@RequestBody User user) {
        try {
            User userToDelete = userEntryService.getUserByUsername(user.getUserName());
            if (user != null) {
                userEntryService.deleteEntryByUserName(userToDelete);
                return new ResponseEntity<>("User entry deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEntryByUserName(@RequestBody User updatedEntry) {
        User oldEntry = userEntryService.getUserByUsername(updatedEntry.getUserName());
        if (oldEntry != null) {
            oldEntry.setUserName(updatedEntry.getUserName().isEmpty() ? oldEntry.getUserName() : updatedEntry.getUserName());
            oldEntry.setUserPassword(updatedEntry.getUserPassword().isEmpty() ? oldEntry.getUserPassword() : updatedEntry.getUserPassword());
            userEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>("User entry updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
        }
    }


}
