package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.service.JournalUserEntryService;
import com.springboot.SpringBootProject.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/journalUser")
public class JournalUserEntryController {

    @Autowired
    private JournalUserEntryService journalEntryService;

    @Autowired
    private UserEntryService userEntryService;



    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User byUserName = userEntryService.getUserByUsername(userName);
        List<JournalEntry> allEntries = byUserName.getJournalEntries();
        if(allEntries.isEmpty()){
            return new ResponseEntity<>("No journal entries found for user: " + userName, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @PostMapping("/create/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry, @PathVariable String userName) {
        try{
            journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>("Journal entry added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{userName}/{id}")
    public ResponseEntity<?> deleteEntryByUserName(@PathVariable ObjectId id, @PathVariable String userName) {
        try {
            journalEntryService.deleteEntryById(id, userName);
            return new ResponseEntity<>("Journal entry deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{userName}/{id}")
    public ResponseEntity<?> addExistingJounalToUser(@PathVariable String userName, @PathVariable ObjectId id) {
        JournalEntry journalEntry = journalEntryService.getEntryById(id).orElse(null);
        if (journalEntry != null) {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.addExistingJounalToUser(journalEntry, userName);
            return new ResponseEntity<>("Journal entry updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }
    }
}
