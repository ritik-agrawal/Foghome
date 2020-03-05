package com.foghome.v1.controller;

import com.foghome.v1.represents.Response;
import com.foghome.v1.sevices.FirebaseServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

@Controller
public class WebController {

    private static Logger Log=Logger.getLogger(WebController.class);
    @Autowired
    private FirebaseServices firebaseServices;

    @GetMapping("/")
    public String getHome(){
        return "home";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/{userId}/homes")
    public Response listHomes(@PathVariable String userId) throws ExecutionException, InterruptedException {
        Log.info("Getting homes of userId:"+userId);
        return firebaseServices.listhomes(userId);
    }
}
