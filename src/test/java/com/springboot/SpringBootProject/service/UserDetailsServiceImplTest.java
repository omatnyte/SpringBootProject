package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import com.springboot.SpringBootProject.repository.UserEntryRepository;
import com.springboot.SpringBootProject.repository.UserSpringSecurityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


//to load full application context use commented annotation
//@SpringBootTest
public class UserDetailsServiceImplTest {

    //@Autowired
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    //@MockBean
    @Mock
    private UserSpringSecurityRepository userEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock and @InjectMocks
    }

    @Test
    void loadUserByUsername() {
        when(userEntryRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(UserSpringSecurity.builder()
                .userName("omkar")
                .userPassword("omkar")
                        .roles(java.util.List.of("USER","ADMIN"))
                .build());
        UserDetails user = userDetailsService.loadUserByUsername("omkar");
        assertNotNull(user);
    }
}
