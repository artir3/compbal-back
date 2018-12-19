package com.arma.inz.compcal.bankaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(value = "DELETE FROM contractor_bank_accounts b WHERE b.bank_accounts_id = :id", nativeQuery = true)
    void deleteFromContractorBankAccounts(@Param("id") Long id);

    @Query(value = "DELETE FROM base_user_bank_accounts b WHERE b.bank_accounts_id = :id", nativeQuery = true)
    void deleteFromBaseUserBankAccount(@Param("id") Long id);
}
