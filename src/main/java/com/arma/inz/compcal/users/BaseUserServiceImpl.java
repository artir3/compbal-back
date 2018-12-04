package com.arma.inz.compcal.users;

import com.arma.inz.compcal.mail.EmailService;
import com.arma.inz.compcal.mail.MailSender;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import com.arma.inz.compcal.users.enums.RolesEnum;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

    @Autowired
    private EmailService emailService;

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
        entity.setActive(Boolean.FALSE);
        entity.setRoles(RolesEnum.USER);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        BaseUser save = baseUserRepository.save(entity);
        sendEmailWithAuthorizationHash(entity.getEmail(), entity.getHash());
        return new ResponseEntity( save != null, HttpStatus.OK);
    }

    private void sendEmailWithAuthorizationHash(String email, String hash) {
        emailService.sendActivationEmail(email, hash);
    }

    @Override
    public ResponseEntity login(@RequestBody UserLoginDTO user) {
        String hash = Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity != null && entity.isActive(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity loginByHash(@RequestBody String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity != null && entity.isActive(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(@RequestBody String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return new ResponseEntity( entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(UserDTO userDTO) {
        Optional<BaseUser> optional = baseUserRepository.findById(userDTO.getId());
        if (optional != null){
            BaseUser entity = optional.get();
            BeanUtils.copyProperties(userDTO, entity, "id", "email", "nip", "createdAt");
            entity.setModifiedAt(LocalDateTime.now());
            baseUserRepository.save(entity);
        }
        return new ResponseEntity( optional != null, HttpStatus.OK);

    }

    @Override
    public ResponseEntity authorize(String authorizationHash) {
        BaseUser entity = baseUserRepository.findOneByHash(authorizationHash);
        if (entity != null) {
            entity.setActive(Boolean.TRUE);
            baseUserRepository.save(entity);
        }
        return new ResponseEntity( entity != null, HttpStatus.OK);
    }

}