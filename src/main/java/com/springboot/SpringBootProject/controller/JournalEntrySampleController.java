package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.JournalEntrySample;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Sample/journal")
public class JournalEntrySampleController {

    private Map<String, JournalEntrySample> journalEntries = new HashMap<>();

    @GetMapping()
    public List<JournalEntrySample> getAllEntries() {
        // return journalEntries.values().stream().toList();
        return new ArrayList<>(journalEntries.values());
    }

    @GetMapping("/id/{id}")
    public JournalEntrySample getEntryById(@PathVariable long id) {
        return journalEntries.get(String.valueOf(id));
    }

    @PostMapping("/create")
    public String createEntry(@RequestBody JournalEntrySample entry) {
        journalEntries.put(entry.getId(), entry);
        return "Journal entry added successfully";
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteEntry(@PathVariable long id) {
        journalEntries.remove(String.valueOf(id));
        return "Journal entry deleted successfully";
    }

    @PutMapping("/update/id/{id}")
    public String updateEntry(@PathVariable long id, @RequestBody JournalEntrySample updatedEntry) {
        journalEntries.put(String.valueOf(id), updatedEntry);
        return "Journal entry updated successfully";
    }
}
