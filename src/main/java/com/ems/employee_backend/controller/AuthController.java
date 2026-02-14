//package com.ems.employee_backend.controller;
//
//import com.ems.employee_backend.entity.User;
//import com.ems.employee_backend.repository.UserRepository;
//import com.ems.employee_backend.security.JwtUtil;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin
//public class AuthController {
//
//    private final UserRepository repo;
//    private final PasswordEncoder encoder;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(UserRepository repo,
//                          PasswordEncoder encoder,
//                          JwtUtil jwtUtil) {
//        this.repo = repo;
//        this.encoder = encoder;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setRole("USER");
//        repo.save(user);
//        return "User registered";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        User dbUser = repo.findByUsername(user.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        return jwtUtil.generateToken(dbUser.getUsername());
//    }
//}
//
