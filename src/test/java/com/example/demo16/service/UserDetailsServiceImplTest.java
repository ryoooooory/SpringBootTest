package com.example.demo16.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import com.example.demo16.model.SiteUser;
import com.example.demo16.repository.SiteUserRepository;
import com.example.demo16.util.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
@Transactional
public class UserDetailsServiceImplTest {
    
    @Autowired
    SiteUserRepository repository;

    @Autowired
    UserDetailsServiceImpl service;

    @Test
    @DisplayName("ユーザ名が存在するとき、ユーザ詳細を取得する")
    void whenUsernameExists_expextToGetUserDetails() {

        SiteUser user = new SiteUser();
        user.setUsername("大嶋");
        user.setPassword("password");
        user.setEmail("oshima@example.com");
        user.setRole(Role.USER.name());
        repository.save(user);

        UserDetails actual = service.loadUserByUsername("大嶋");

        assertEquals(user.getUsername(), actual.getUsername());
    }

    @Test
    @DisplayName("ユーザが存在しないとき例外をスローします")
    void whenUsernameDoesNotExist_throwException() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("加藤"));
    }
}
