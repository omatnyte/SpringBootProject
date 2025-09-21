package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.JournalEntry;
import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalUserEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserEntryService userEntryService;


    // @Transactional
    // Commented out because MongoDB standalone server does not support transactions.
    // Transactions in MongoDB require a replica set or sharded cluster.
    // Since this instance is running as a standalone mongod, using @Transactional will cause errors.
    public void saveEntry(JournalEntry entry, String userName) {
        entry.setDate(LocalDateTime.now());
        User byUserName = userEntryService.getUserByUsername(userName);
        JournalEntry saved = journalEntryRepository.save(entry);
        byUserName.getJournalEntries().add(saved);
        userEntryService.saveEntry(byUserName);
    }

    public void addExistingJounalToUser(JournalEntry entry, String userName) {
        User byUserName = userEntryService.getUserByUsername(userName);
        byUserName.getJournalEntries().add(entry);
        userEntryService.saveEntry(byUserName);
    }


    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id, String userName) {
        User byUserName = userEntryService.getUserByUsername(userName);
        byUserName.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userEntryService.saveEntry(byUserName);
        journalEntryRepository.deleteById(id);
    }
}
