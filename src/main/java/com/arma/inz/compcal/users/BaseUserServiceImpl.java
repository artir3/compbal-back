package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@Log
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostMapping("/user/email")
    public BaseUser findUserByEmail(@RequestBody BaseUser user) {
        return baseUserRepository.findByEmail(user.getEmail());
    }

    @Override
    public ResponseEntity registration(@RequestBody UserRegistrationDTO user) {
        BaseUser entity = new BaseUser();
        BeanUtils.copyProperties(user, entity);
        entity.setHash(Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes()));
        entity.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        entity.setActive(Boolean.TRUE);
        entity.setRoles(RolesEnum.USER);
        BaseUser save = baseUserRepository.save(entity);
        return new ResponseEntity( save != null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity login(@RequestBody UserLoginDTO user) {
        String hash = Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity != null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity loginByHash(@RequestBody String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity != null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(@RequestBody String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(UserDTO userDTO) {
        Optional<BaseUser> entity = baseUserRepository.findById(userDTO.getId());
        if (entity != null){
            BaseUser baseUser = entity.get();
            BeanUtils.copyProperties(userDTO, baseUser, "id", "email", "nip");
            baseUserRepository.save(baseUser);
        }
        return new ResponseEntity( entity != null, HttpStatus.OK);

    }

    @Override
    public ResponseEntity authorize(String authorizationHash) {
        return new ResponseEntity( false, HttpStatus.OK);
    }

}