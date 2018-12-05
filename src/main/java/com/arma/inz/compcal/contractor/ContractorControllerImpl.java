package com.arma.inz.compcal.contractor;


import org.springframework.beans.factory.annotation.Autowired;

public class ContractorControllerImpl implements ContractorController {

    @Autowired
    private ContractorRepository contractorRepository;
}
