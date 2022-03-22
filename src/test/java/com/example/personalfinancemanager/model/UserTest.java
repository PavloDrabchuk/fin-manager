package com.example.personalfinancemanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    void testUserConstructor() {
        User user = new User("User", "$2a$12$85YoU7uqDUESPzuhhsViAuitRq9gthz.O/SlIOBXdKWpTGQofyLKC");

        assertAll("user",
                () -> assertEquals("User", user.getUsername()),
                () -> assertEquals(
                        "$2a$12$85YoU7uqDUESPzuhhsViAuitRq9gthz.O/SlIOBXdKWpTGQofyLKC",
                        user.getPassword())
        );
    }

    @Test
    void testUserDefaultConstructor() {
        User user = new User();

        user.setId(1L);
        user.setUsername("User");
        user.setPassword("$2a$12$85YoU7uqDUESPzuhhsViAuitRq9gthz.O/SlIOBXdKWpTGQofyLKC");

        assertAll("user",
                () -> assertEquals(1L, user.getId()),
                () -> assertEquals("User", user.getUsername()),
                () -> assertEquals(
                        "$2a$12$85YoU7uqDUESPzuhhsViAuitRq9gthz.O/SlIOBXdKWpTGQofyLKC",
                        user.getPassword())
        );
    }
}
