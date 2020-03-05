package com.foghome.v1.controller;

import com.foghome.v1.represents.*;
import com.foghome.v1.sevices.FirebaseServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Controller
public class FogController {

    @Autowired
    private FirebaseServices firebaseServices;
    private static Logger Log=Logger.getLogger(FogController.class);
//post Requests
    @PostMapping("/signup")
    public String signup(@ModelAttribute(name = "signup") User user){
        Log.info("Addig a User:"+user.getUsername());
        firebaseServices.addUser(user);
        return "home";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute(name = "login") Login login, Model model) throws ExecutionException, InterruptedException {
        Log.info("Logging in User: "+login.getUserName());
        Response response=firebaseServices.loginUser(login);
        if (response.getResultKey()!=null){
            model.addAttribute("login",login);
            Log.info(response.getList());
            model.addAttribute("list",response.getList());
            return "/homepanel";
        }
        model.addAttribute("error",true);
        model.addAttribute("response",response);
        return "/login";
    }

    @PostMapping("/{userId}/homes/addHome")
    public Response createHome(@RequestBody Home home, @PathVariable String userId){
        Log.info("UserID("+userId+") adding home:"+home.getHomeName());
        return firebaseServices.addHome(home, userId);
    }

    @PostMapping("/{userId}/homes/{homeId}/grantAccess")
    public Response grantAccess(@RequestBody GrantAccess grantAccess, @PathVariable String userId, @PathVariable String homeId) throws ExecutionException, InterruptedException {
        return firebaseServices.grantAccessTo(grantAccess,userId,homeId);
    }
}
