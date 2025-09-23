package com.springboot.SpringBootProject.service;

import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import com.springboot.SpringBootProject.repository.UserEntryRepository;
import com.springboot.SpringBootProject.repository.UserSpringSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserSpringSecurityRepository userSpringSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSpringSecurity user = userSpringSecurityRepository.findByUserName(username);
        if(user != null) {
            UserDetails userDetails =  org.springframework.security.core.userdetails.User.builder().
                    username(user.getUserName()).password(user.getUserPassword()).
                    roles(user.getRoles().toArray(new String[0])).build();

            return userDetails;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
