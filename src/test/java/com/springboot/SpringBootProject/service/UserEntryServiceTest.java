package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.User;
import com.springboot.SpringBootProject.repository.UserEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserEntryServiceTest {

    @Autowired
    private UserEntryRepository userEntryRepository;

    @Test
    public void findByUserName() {
        User user = userEntryRepository.findByUserName("omkar");
        assertEquals("omkar", user.getUserName());
    }

    @Test void NotNull(){
        assertNotNull(userEntryRepository.findByUserName("omkar"));
    }

    @ParameterizedTest
    @CsvSource({
        "omkar, omkar",
        "omkar1, omkar1",
        "omkar2, omkar2"
    })
    public void testFindByUserNameParameterized(String userName, String expected) {
        User user = userEntryRepository.findByUserName(userName);
        assertEquals(expected, user.getUserName(), "username should match the expected value");
    }

    @ParameterizedTest
    @ValueSource(strings = {"omkar", "omkar1", "omkar2"})
    public void testFindByUserNameValueSource(String userName) {
        User user = userEntryRepository.findByUserName(userName);
        assertNotNull(user, "User should not be null");
    }
}
