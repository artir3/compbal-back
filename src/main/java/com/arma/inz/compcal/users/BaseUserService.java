package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface BaseUserService {
    @PostMapping("/registration")
    ResponseEntity registration(@RequestBody UserRegistrationDTO user);

    @PostMapping("/login")
    ResponseEntity login(@RequestBody UserLoginDTO user);

    @GetMapping("/login/hash")
    ResponseEntity loginHash(@RequestHeader(value="Authorization") String authorization);

    @CrossOrigin
    @GetMapping("/")
    ResponseEntity get(@RequestHeader(value="Authorization") String authorization);

    @CrossOrigin
    @PutMapping("/")
    ResponseEntity update(@RequestBody UserDTO userDTO);

    @GetMapping("/authorize/{authorizationHash}")
    ResponseEntity authorize(@PathVariable String authorizationHash);
}
