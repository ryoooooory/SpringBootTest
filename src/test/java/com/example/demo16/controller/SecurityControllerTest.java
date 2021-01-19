package com.example.demo16.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import com.example.demo16.model.SiteUser;
import com.example.demo16.util.Role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("登録エラーがあるとき、エラー表示することを期待します")
    void whenThereIsRegistrationError_expectToSeeErrors() throws Exception {
        mockMvc.perform(
            post("/register")
            .flashAttr("user", new SiteUser())
            .with(csrf())
            )
            .andExpect(model().hasErrors())
            .andExpect(view().name("register"));
    }

    @Test
    @DisplayName("管理ユーザーとして登録するとき成功することを期待します。")
    void whenRegisteringAsAdminUser_expectToSucceed() throws Exception {
        SiteUser user = new SiteUser();
        user.setUsername("管理者ユーザー");
        user.setPassword("password");
        user.setEmail("admin@example.com");
        user.setGender(0);
        user.setAdmin(true);
        user.setRole(Role.ADMIN.name());

        mockMvc.perform(post("/register")
            .flashAttr("user", user).with(csrf()))
            .andExpect(model().hasNoErrors())
            .andExpect(redirectedUrl("/login?register"))
            .andExpect(status().isFound());
    }

    @Test
    @DisplayName("管理ユーザーとしてログインしたとき、ユーザー一覧を表示することを期待します。")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void whenLoggInAsAdminUser_expetToSeeListOfUsers() throws Exception {
        mockMvc.perform(get("/admin/list"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("ユーザ一覧")))
        .andExpect(view().name("list"));
    }
}
