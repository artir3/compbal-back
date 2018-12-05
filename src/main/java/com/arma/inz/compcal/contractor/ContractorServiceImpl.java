package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractorServiceImpl implements ContractorService {

    @Autowired
    private ContractorController contractorController;

    @Autowired
    private AuthorizationHeaderUtils header;
}
