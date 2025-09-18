package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;



    @GetMapping()
    public List<JournalEntry> getAllEntries() {

        return journalEntryService.getAllEntries();
    }

    @GetMapping("/id/{id}")
    public JournalEntry getEntryById(@PathVariable ObjectId id) {
        return journalEntryService.getEntryById(id).orElse(null);
    }

    @PostMapping("/create")
    public String createEntry(@RequestBody JournalEntry entry) {
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return "Journal entry added successfully";
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteEntryById(id);
        return "Journal entry deleted successfully";
    }

    @PutMapping("/update/id/{id}")
    public String updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry updatedEntry) {
        JournalEntry oldEntry = journalEntryService.getEntryById(id).orElse(null);
        if (oldEntry != null) {
            oldEntry.setTitle((updatedEntry.getTitle() == null || updatedEntry.getTitle().isEmpty()) ? oldEntry.getTitle() : updatedEntry.getTitle());
            oldEntry.setContent((updatedEntry.getContent() == null || updatedEntry.getContent().isEmpty()) ? oldEntry.getContent() : updatedEntry.getContent());
            oldEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(oldEntry);
            return "Journal entry updated Succesfully";
        } else {
            return "Journal entry not found";
        }
    }
}
