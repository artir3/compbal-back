package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/user")
public interface BaseUserService {
    @PostMapping("/registration")
    ResponseEntity registration(@RequestBody UserRegistrationDTO user);

    @PostMapping("/login")
    ResponseEntity login(@RequestBody UserLoginDTO user);

    @GetMapping("/loginbyhash")
    ResponseEntity loginByHash(@RequestBody String hash);

    @GetMapping("/")
    ResponseEntity get(@RequestBody String hash);

    @PutMapping("/")
    ResponseEntity update(@RequestBody UserDTO userDTO);

    @GetMapping("/authorize/{authorizationHash}")
    ResponseEntity authorize(@PathVariable String authorizationHash);
}
