package com.arma.inz.compcal.database;

import com.arma.inz.compcal.MapperToObject;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorSelectDTO;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.mail.Email;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseModelsFromJsons {
    public static ActivateDTO activateFullDTO() {
        return (ActivateDTO) MapperToObject.FileToObject(new ActivateDTO(), "baseUser/");
    }

    public static ActivateDTO activateDTOFromAngular() {
        return (ActivateDTO) MapperToObject.FileToObject(new ActivateDTO(), "baseUser/", "ActivateDTOFromAngular");
    }

    public static BaseUser baseUser() {
        BaseUser object = new BaseUser();
        object = (BaseUser) MapperToObject.FileToObject(object, "baseUser/");
        object.setCreatedAt(LocalDateTime.now());
        object.setModifiedAt(LocalDateTime.now());
        return object;
    }

    public static BaseUser baseUserPassword() {
        BaseUser object = new BaseUser();
        object = (BaseUser) MapperToObject.FileToObject(object, "baseUser/", "BaseUserPassword");
        object.setCreatedAt(LocalDateTime.now());
        object.setModifiedAt(LocalDateTime.now());
        return object;
    }

    public static BaseUser baseUserActivated() {
        BaseUser object = baseUser();
        object.setActive(Boolean.TRUE);
        return object;
    }

    public static Email email(BaseUser baseUser) {
        Email object = (Email) MapperToObject.FileToObject(new Email(), "baseUser/");
        object.setCreatedAt(LocalDateTime.now());
        object.addBaseUser(baseUser);
        return object;
    }

    public static UserDTO userDTO() {
        return (UserDTO) MapperToObject.FileToObject(new UserDTO(), "baseUser/", "UserDTO");
    }

    public static UserDTO userDTOPassword() {
        return (UserDTO) MapperToObject.FileToObject(new UserDTO(), "baseUser/", "UserDTOPassword");
    }

    public static UserLoginDTO userLoginDTO() {
        return (UserLoginDTO) MapperToObject.FileToObject(new UserLoginDTO(), "baseUser/");
    }

    public static UserRegistrationDTO userRegistrationDTO() {
        return (UserRegistrationDTO) MapperToObject.FileToObject(new UserRegistrationDTO(), "baseUser/");
    }

    //------------------- main folder

    public static String hash() {
        return (String) MapperToObject.FileToObject(new String(), "", "Hash");
    }

    public static String hashUpdated() {
        return (String) MapperToObject.FileToObject(new String(), "", "HashUpdated");
    }

    public static String authorization() {
        return (String) MapperToObject.FileToObject(new String(), "", "Authorization");
    }

    //------------------- contractor

    public static ContractorDTO contractorDTO(int fileNumber) {
        return (ContractorDTO) MapperToObject.FileToObject(new ContractorDTO(), "contractor/", "ContractorDTO" + fileNumber);
    }

    public static List<ContractorDTO> contractorDTOList() {
        List<ContractorDTO> result = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            result.add((ContractorDTO) MapperToObject.FileToObject(new ContractorDTO(), "contractor/", "ContractorDTO" + i));
        }
        return result;
    }

    public static ContractorFilterDTO ContractorFilterDTOEmpty() {
        return (ContractorFilterDTO) MapperToObject.FileToObject(new ContractorFilterDTO(), "contractor/", "ContractorFilterDTOEmpty");
    }

    public static ContractorFilterDTO ContractorFilterDTOFContractor() {
        return (ContractorFilterDTO) MapperToObject.FileToObject(new ContractorFilterDTO(), "contractor/", "ContractorFilterDTOFContractor");
    }

    public static ContractorFilterDTO ContractorFilterDTOFContractorBrand() {
        return (ContractorFilterDTO) MapperToObject.FileToObject(new ContractorFilterDTO(), "contractor/", "ContractorFilterDTOFContractorBrand");
    }

    public static List<ContractorDTO> ContractorMiniDTO() {
        List<ContractorDTO> filterDTO = (List<ContractorDTO>) MapperToObject.FileToObject(new ArrayList<ContractorDTO>(), "contractor/", "ContractorMiniDTO");
        return filterDTO;
    }

    public static List<ContractorDTO> ContractorMiniDTOList() {
        List<ContractorDTO> filterDTO = (List<ContractorDTO>) MapperToObject.FileToObject(new ArrayList<ContractorDTO>(), "contractor/", "ContractorMiniDTOList");
        return filterDTO;
    }

    public static List<ContractorDTO> ContractorMiniDTOListContractorBrand() {
        List<ContractorDTO> filterDTO = (List<ContractorDTO>) MapperToObject.FileToObject(new ArrayList<ContractorDTO>(), "contractor/", "ContractorMiniDTOListContractorBrand");
        return filterDTO;
    }

    public static List<ContractorDTO> ContractorMiniDTOListFContactor() {
        List<ContractorDTO> filterDTO = (List<ContractorDTO>) MapperToObject.FileToObject(new ArrayList<ContractorDTO>(), "contractor/", "ContractorMiniDTOListFContactor");
        return filterDTO;
    }

    public static List<ContractorSelectDTO> ContractorSelectDTO() {
        List<ContractorSelectDTO> filterDTO = (List<ContractorSelectDTO>) MapperToObject.FileToObject(new ArrayList<ContractorSelectDTO>(), "contractor/", "ContractorSelectDTO");
        return filterDTO;
    }

    //------------------- kpir print

    public static KpirFilterDTO kpirFilterDTOPrint() {
        return (KpirFilterDTO) MapperToObject.FileToObject(new KpirFilterDTO(), "", "KpirFilterDTOPrint");
    }

    public static KpirCreateDTO kpirCreateDTO(String folder, int fileNumber) {
        return (KpirCreateDTO) MapperToObject.FileToObject(new KpirCreateDTO(), folder, "KpirCreateDTO" + fileNumber);
    }

    public static List<KpirCreateDTO> kpirCreateDTOList() {
        List<KpirCreateDTO> result = new ArrayList<>();
        result.addAll(kpirCreateDTOIncomeList());
        result.addAll(kpirCreateDTOCostsList());
        return result;
    }

    //------------------- kpir income

    public static KpirCreateDTO kpirCreateDTOIncome(int fileNumber) {
        KpirCreateDTO kpirCreateDTO = (KpirCreateDTO) MapperToObject.FileToObject(new KpirCreateDTO(), "kpirIncome/", "KpirCreateDTO" + fileNumber);
        kpirCreateDTO.setEconomicEventDate(randomLocalDateTime());
        kpirCreateDTO.setPaymentDateMin(kpirCreateDTO.getEconomicEventDate().plusDays(20));
        kpirCreateDTO.setPaymentDateMax(kpirCreateDTO.getPaymentDateMin().plusDays(20));
        return kpirCreateDTO;
    }

    public static List<KpirCreateDTO> kpirCreateDTOIncomeList() {
        List<KpirCreateDTO> result = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            result.add(kpirCreateDTOIncome(i));
        }
        return result;
    }

    public static KpirFilterDTO KpirFilterDTOIncomeEmpty() {
        return (KpirFilterDTO) MapperToObject.FileToObject(new KpirFilterDTO(), "kpirIncome/", "KpirFilterDTOEmpty");
    }

    public static KpirFilterDTO KpirFilterDTOYearContrNotPay() {
        return (KpirFilterDTO) MapperToObject.FileToObject(new KpirFilterDTO(), "kpirIncome/", "KpirFilterDTOYearContrNotPay");
    }

    public static List<KpirCreateDTO> KpirDTOIncomeListYearContrNotPay() {
        List<KpirCreateDTO> filterDTO = (List<KpirCreateDTO>) MapperToObject.FileToObject(new ArrayList<KpirCreateDTO>(), "kpirIncome/", "KpirDTOListYearContrNotPay");
        return filterDTO;
    }
    //------------------- kpir outcome

    public static KpirCreateDTO kpirCreateDTOCosts(int fileNumber) {
        KpirCreateDTO kpirCreateDTO = (KpirCreateDTO) MapperToObject.FileToObject(new KpirCreateDTO(), "kpirCosts/", "KpirCreateDTO" + fileNumber);
        kpirCreateDTO.setEconomicEventDate(randomLocalDateTime());
        kpirCreateDTO.setPaymentDateMin(kpirCreateDTO.getEconomicEventDate().plusDays(20));
        kpirCreateDTO.setPaymentDateMax(kpirCreateDTO.getPaymentDateMin().plusDays(20));
        return kpirCreateDTO;
    }

    public static List<KpirCreateDTO> kpirCreateDTOCostsList() {
        List<KpirCreateDTO> result = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            result.add(kpirCreateDTOCosts(i));
        }
        return result;
    }

    public static KpirFilterDTO kpirFilterDTOCostsEmpty() {
        return (KpirFilterDTO) MapperToObject.FileToObject(new KpirFilterDTO(), "kpirCosts/", "KpirFilterDTOEmpty");
    }

    public static KpirFilterDTO kpirFilterDTOCostsNotPayed() {
        return (KpirFilterDTO) MapperToObject.FileToObject(new KpirFilterDTO(), "kpirCosts/", "KpirFilterDTONotPayed");
    }

    public static List<KpirCreateDTO> kpirDTOCostsListNotPayed() {
        List<KpirCreateDTO> filterDTO = (List<KpirCreateDTO>) MapperToObject.FileToObject(new ArrayList<KpirCreateDTO>(), "kpirCosts/", "KpirDTOListNotPayed");
        return filterDTO;
    }

//    ---------------------------

    private static LocalDateTime randomLocalDateTime() {
        return randomLocalDateTime(LocalDateTime.now());
    }

    private static LocalDateTime randomLocalDateTime(LocalDateTime maxLocalDateTime) {
        LocalDate start = LocalDate.of(2018, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, maxLocalDateTime.toLocalDate());
        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
        return LocalDateTime.of(randomDate, LocalTime.MIDNIGHT);
    }
}

