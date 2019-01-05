package com.arma.inz.compcal.users;

import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import com.arma.inz.compcal.users.enums.RolesEnum;
import com.arma.inz.compcal.users.enums.TaxFormEnum;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Log
@AllArgsConstructor
@Controller
public class BaseUserControllerImpl implements BaseUserController {
    private final BaseUserRepository baseUserRepository;
    private final BankAccountController bankAccountController;
    private final BaseUserMailSender baseUserMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean registration(UserRegistrationDTO user) {
        BaseUser existUser = baseUserRepository.findByEmail(user.getEmail());
        if (existUser == null){
            return false;
        }
        BaseUser entity = new BaseUser();
        BeanUtils.copyProperties(user, entity);
        entity.setHash(Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes()));
        entity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        entity.setActive(Boolean.FALSE);
        entity.setRoles(RolesEnum.USER);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setTaxForm(TaxFormEnum.valueOf(user.getTaxForm()));
        BaseUser save = baseUserRepository.save(entity);
        sendEmailWithAuthorizationHash(entity.getEmail(), entity.getHash());
        return save != null;
    }

    private void sendEmailWithAuthorizationHash(String email, String hash) {
        BaseUser user = baseUserRepository.findOneByHash(hash);
        baseUserMailSender.sendActivationEmail(email, hash, user);
    }

    @Override
    public boolean login(UserLoginDTO user) {
        String hash = Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return entity != null && entity.isActive();
    }

    @Override
    public boolean loginByHash(String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        return entity != null && entity.isActive();
    }

    @Override
    public UserDTO getUserDTO(String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        UserDTO result = new UserDTO();
        BeanUtils.copyProperties(entity, result, "password");
        result.setTaxForm(entity.getTaxForm().name());

        result.setBankAccountSet(bankAccountController.copyToDTO(entity.getBankAccounts()));
        return result;
    }

    @Override
    public BaseUser getBaseUser(String hash) {
        return baseUserRepository.findOneByHash(hash);
    }

    @Override
    public boolean updateBaseUser(UserDTO userDTO) {
        Optional<BaseUser> optional = baseUserRepository.findById(userDTO.getId());
        if (optional != null){
            BaseUser entity = optional.get();
            BeanUtils.copyProperties(userDTO, entity, "id", "email", "nip", "createdAt", "bankAccountSet", "password");
            entity.setTaxForm(TaxFormEnum.valueOf(userDTO.getTaxForm()));
            entity.setModifiedAt(LocalDateTime.now());

            if (userDTO.getPassword() != null) {
                entity.setHash(Base64.getEncoder().encodeToString((userDTO.getEmail() + ":" + userDTO.getPassword()).getBytes()));
                entity.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            }

            entity = baseUserRepository.save(entity);
            bankAccountController.saveOrUpdate(userDTO.getBankAccountSet(), entity);
        }
        return optional != null;
    }

    @Override
    public boolean authorize(ActivateDTO activateDTO) {
        int nAdditionalSigns = (activateDTO.getCode().length() / 3) % 4;
        String code = activateDTO.getCode() + String.join("", Collections.nCopies(nAdditionalSigns, "="));
        BaseUser entity = baseUserRepository.findOneByHash(code);
        if (entity != null) {
            entity.setActive(Boolean.TRUE);
            baseUserRepository.save(entity);
        }
        return entity != null;
    }

    @Override
    public LocalDateTime registrationDate(String hash) {
        BaseUser baseUser = getBaseUser(hash);
        return baseUser.getCreatedAt();
    }

}