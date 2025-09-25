package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import com.springboot.SpringBootProject.service.JournalUserSecurityService;
import com.springboot.SpringBootProject.service.UserSecurityService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("security/journalUser")
public class JournalUserSecurityController {

    @Autowired
    private JournalUserSecurityService journalEntryService;

    @Autowired
    private UserSecurityService userEntryService;



    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserSpringSecurity byUserName = userEntryService.getUserByUsername(currentUserName);
        List<JournalEntry> allEntries = byUserName.getJournalEntries();
        if(allEntries.isEmpty()){
            return new ResponseEntity<>("No journal entries found for user: " + currentUserName, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllJournalEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(journalEntryService.getAllEntries(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Access denied: User does not have ADMIN role", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserSpringSecurity byUserName = userEntryService.getUserByUsername(userName);
        List<JournalEntry> journalEntries = byUserName.getJournalEntries().stream().filter(e -> e.getId().equals(id)).toList();

        if(!journalEntries.isEmpty()){
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>("Journal entry added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEntryByUserName(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            journalEntryService.deleteEntryById(id, userName);
            return new ResponseEntity<>("Journal entry deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> addExistingJounalToUser(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
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
