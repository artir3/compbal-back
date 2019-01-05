package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface BaseUserService {
    @CrossOrigin
    @PostMapping("/registration")
    ResponseEntity registration(@RequestBody UserRegistrationDTO user);

    @CrossOrigin
    @PostMapping("/login")
    ResponseEntity login(@RequestBody UserLoginDTO user);

    @CrossOrigin
    @GetMapping("/login/hash")
    ResponseEntity loginHash(@RequestHeader(value="Authorization") String authorization);

    @CrossOrigin
    @GetMapping("/")
    ResponseEntity get(@RequestHeader(value="Authorization") String authorization);

    @CrossOrigin
    @PutMapping("/")
    ResponseEntity update(@RequestBody UserDTO userDTO);

    @CrossOrigin
    @PostMapping("/authorize")
    ResponseEntity authorize(@RequestBody ActivateDTO activateDTO);

    @CrossOrigin
    @GetMapping("/authorize/{hash}")
    String authorizeOld(@PathVariable String hash);

    @CrossOrigin
    @GetMapping("/registration/date")
    ResponseEntity registrationDate(@RequestHeader(value="Authorization") String authorization);
}
