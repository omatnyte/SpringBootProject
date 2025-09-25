package com.springboot.SpringBootProject.controller;


import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import com.springboot.SpringBootProject.service.UserSecurityService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("security/user")
public class UserSecurityController {

    @Autowired
    private UserSecurityService userSecurityService;

    @GetMapping()
    public ResponseEntity<?> getAllEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(userSecurityService.getAllEntries(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Access denied: User does not have ADMIN role", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/adminCheck")
    public ResponseEntity<?> getAllEntriesWithAdminCheck() {
        List<UserSpringSecurity> allEntries = userSecurityService.getAllEntries();
        if(allEntries != null){
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("No user entries found", HttpStatus.NOT_FOUND);
        }
        }

    @GetMapping("/annotationCheck")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEntriesWithAnnotationCheck() {
        return new ResponseEntity<>(userSecurityService.getAllEntries(), HttpStatus.OK);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Optional<UserSpringSecurity> entry = userSecurityService.getEntryById(id);

        if(entry.isPresent()){
            System.out.println("Hello");
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/username")
    public ResponseEntity<?> getEntryByLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserSpringSecurity user = userSecurityService.getUserByUsername(currentUserName);

        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody UserSpringSecurity entry) {

        try{
            userSecurityService.saveNewEntry(entry);
            return new ResponseEntity<>("User entry added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        try {
            userSecurityService.deleteEntryById(id);
            return new ResponseEntity<>("User entry deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEntryByUserName(@RequestBody UserSpringSecurity user) {
        try {
            UserSpringSecurity userToDelete = userSecurityService.getUserByUsername(user.getUserName());
            if (user != null) {
                userSecurityService.deleteEntryByUserName(userToDelete);
                return new ResponseEntity<>("User entry deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user entry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEntryByUserName(@RequestBody UserSpringSecurity updatedEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserSpringSecurity oldEntry = userSecurityService.getUserByUsername(currentUserName);
        if (oldEntry != null) {
            oldEntry.setUserName(updatedEntry.getUserName().isEmpty() ? oldEntry.getUserName() : updatedEntry.getUserName());
            oldEntry.setUserPassword(updatedEntry.getUserPassword().isEmpty() ? oldEntry.getUserPassword() : updatedEntry.getUserPassword());
            userSecurityService.saveEntry(oldEntry);
            return new ResponseEntity<>("User entry updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User entry not found", HttpStatus.NOT_FOUND);
        }
    }


}
