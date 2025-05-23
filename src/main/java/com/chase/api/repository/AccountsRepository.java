package com.chase.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.chase.api.model.Account;

import jakarta.persistence.LockModeType;

@Transactional(readOnly = true)
public interface AccountsRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByAccountNo(Long id);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	@Query("SELECT a FROM account a WHERE a.accountNo = ?1")
	Optional<Account> getAccountForUpdate(Long id);
	
}
