package com.arma.inz.compcal.users;

import com.arma.inz.compcal.currency.CurrencyEnum;
import com.arma.inz.compcal.mail.EmailService;
import com.arma.inz.compcal.users.dto.BankAccountDTO;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import com.arma.inz.compcal.users.enums.RolesEnum;
import com.arma.inz.compcal.users.enums.TaxFormEnum;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log
@Controller
public class BaseUserControllerImpl implements BaseUserController {

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean registration(UserRegistrationDTO user) {
        BaseUser entity = new BaseUser();
        BeanUtils.copyProperties(user, entity);
        entity.setHash(Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes()));
        entity.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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
        emailService.sendActivationEmail(email, hash);
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
    public UserDTO getBaseUser(String hash) {
        BaseUser entity = baseUserRepository.findOneByHash(hash);
        UserDTO result = new UserDTO();
        BeanUtils.copyProperties(entity, result, "password");
        result.setTaxForm(entity.getTaxForm().name());

        Set<BankAccountDTO> bankAccounts = new HashSet<BankAccountDTO>();
        for (BankAccount account: entity.getBankAccountSet()) {
            BankAccountDTO dto = new BankAccountDTO();
            BeanUtils.copyProperties(account, dto);
            dto.setCurrency(account.getCurrency().name());
            bankAccounts.add(dto);
        }
        result.setBankAccountSet(bankAccounts);
        return result;
    }

    @Override
    public boolean updateBaseUser(UserDTO userDTO) {
        Optional<BaseUser> optional = baseUserRepository.findById(userDTO.getId());
        if (optional != null){
            BaseUser entity = optional.get();
            BeanUtils.copyProperties(userDTO, entity, "id", "email", "nip", "createdAt", "bankAccountSet");
            entity.setTaxForm(TaxFormEnum.valueOf(userDTO.getTaxForm()));
            entity.setModifiedAt(LocalDateTime.now());

            if (!StringUtils.isEmpty(userDTO.getPassword())){
                entity.setHash(Base64.getEncoder().encodeToString((userDTO.getEmail() + ":" + userDTO.getPassword()).getBytes()));
                entity.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            }

            baseUserRepository.save(entity);

            for (BankAccountDTO dto:userDTO.getBankAccountSet()) {
                BankAccount bankAccount = null;
                if(dto.getId() != null){
                    bankAccount = bankAccountRepository.findById(dto.getId()).get();
                } else {
                    bankAccount = new BankAccount();
                    bankAccount.setCreatedAt(LocalDateTime.now());
                    bankAccount.setBaseUser(entity);
                }
                BeanUtils.copyProperties(dto, bankAccount, "id", "baseUser");
                bankAccount.setCurrency(CurrencyEnum.valueOf(dto.getCurrency()));
                bankAccount.setModifiedAt(LocalDateTime.now());
                bankAccountRepository.save(bankAccount);
            }
        }
        return optional != null;

    }

    @Override
    public boolean authorize(String authorizationHash) {
        BaseUser entity = baseUserRepository.findOneByHash(authorizationHash);
        if (entity != null) {
            entity.setActive(Boolean.TRUE);
            baseUserRepository.save(entity);
        }
        return entity != null;
    }

    @Override
    public boolean deleteAccount(Long id) {
        if (id == null){ return false; }
        bankAccountRepository.deleteById(id);
        return true;
    }

}