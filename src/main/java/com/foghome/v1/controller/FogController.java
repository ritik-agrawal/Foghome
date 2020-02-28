package com.foghome.v1.controller;

import com.foghome.v1.represents.Login;
import com.foghome.v1.represents.Response;
import com.foghome.v1.represents.User;
import com.foghome.v1.sevices.FirebaseServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
public class FogController {

    @Autowired
    private FirebaseServices firebaseServices;
    private static Logger Log=Logger.getLogger(FogController.class);
    @PostMapping("/signup")
    public void signup(@RequestBody User user){
        Log.info("Addig a User:"+user.getUsername());
        firebaseServices.addUser(user);
    }

    @PostMapping("/login")
    public Response login(@RequestBody Login login) throws ExecutionException, InterruptedException {
        Log.info("Logging in User: "+login.getUserName());
        return firebaseServices.loginUser(login);
    }

    @GetMapping("/{userId}/homes")
    public Response listHomes(@PathVariable String userId) throws ExecutionException, InterruptedException {
        Log.info("Getting homes of userId:"+userId);
        return firebaseServices.listhomes(userId);
    }
}
