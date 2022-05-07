package com.securityjwt.dailycodebuffer.controller;

import com.securityjwt.dailycodebuffer.model.JwtRequest;
import com.securityjwt.dailycodebuffer.model.JwtResponse;
import com.securityjwt.dailycodebuffer.service.UserService;
import com.securityjwt.dailycodebuffer.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired private JwtUtility jwtUtility;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private UserService userService;

    @GetMapping("/")
    public String home(){
        return " Welcome ";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword()
                    )
            );
        }catch ( BadCredentialsException e){
            throw new Exception("Invalid Credentials", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUserName());

        final String token = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

}
