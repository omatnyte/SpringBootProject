package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api-2/journal")
public class JournalEntryResponseEntityController {

    @Autowired
    private JournalEntryService journalEntryService;



    @GetMapping()
    public ResponseEntity<?> getAllEntries() {

        return new ResponseEntity<>(journalEntryService.getAllEntries(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.getEntryById(id);

        if(entry.isPresent()){
            System.out.println("Hello");
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {

        try{
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry);
            return new ResponseEntity<>("Journal entry added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        try {
            journalEntryService.deleteEntryById(id);
            return new ResponseEntity<>("Journal entry deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting journal entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry updatedEntry) {
        JournalEntry oldEntry = journalEntryService.getEntryById(id).orElse(null);
        if (oldEntry != null) {
            oldEntry.setTitle((updatedEntry.getTitle() == null || updatedEntry.getTitle().isEmpty()) ? oldEntry.getTitle() : updatedEntry.getTitle());
            oldEntry.setContent((updatedEntry.getContent() == null || updatedEntry.getContent().isEmpty()) ? oldEntry.getContent() : updatedEntry.getContent());
            oldEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>("Journal entry updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }
    }
}
