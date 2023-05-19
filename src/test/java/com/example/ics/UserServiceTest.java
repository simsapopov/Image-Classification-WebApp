package com.example.ics;

import com.example.ics.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    private UserService userService;
    @Test
    void helloWorldTest(){
        userService = new UserService();
        Assertions.assertEquals("Hello, world!",userService.helloWorld());

    }
    @Test
    void helloWorldTestIsItString(){
        userService = new UserService();
        Assertions.assertEquals("String",userService.helloWorld().getClass().getSimpleName());

    }
}
