package com.myblog7.controller;

import com.myblog7.entity.Role;
import com.myblog7.entity.User;
import com.myblog7.payload.LoginDto;
import com.myblog7.payload.SignUpDto;
import com.myblog7.repositry.RoleRepository;
import com.myblog7.repositry.UserRepository;
import com.myblog7.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository rolRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    //http://localhost:8080/api/auth//signin


    @Autowired
    private JwtTokenProvider tokenProvider;




    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }







    //http://localhost:8080/api/auth//signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        boolean emailExist = userRepo.existsByEmail(signUpDto.getEmail());
        if (emailExist) {
            return new ResponseEntity<>("Email Id Exist", HttpStatus.CREATED);
        }
        boolean usernameExist = userRepo.existsByUsername(signUpDto.getUsername());
        if (usernameExist) {
            return new ResponseEntity<>("Username Id Exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Assign default role "USER" to the new user
        Role userRole = rolRepo.findByName("USER");
        if (userRole != null) {
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }

        userRepo.save(user);
        return new ResponseEntity<>("User is registered", HttpStatus.OK);
    }
}
