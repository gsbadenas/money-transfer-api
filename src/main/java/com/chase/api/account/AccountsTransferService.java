package com.chase.api.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.chase.api.constant.ErrorCode;
import com.chase.api.exception.AccountNotExistException;
import com.chase.api.exception.CheckBalanceException;
import com.chase.api.exception.OverDraftException;
import com.chase.api.exception.SystemException;
import com.chase.api.model.Account;
import com.chase.api.model.TransferRequest;
import com.chase.api.repository.AccountsRepository;

@Service
public class AccountsTransferService {

	private static final Logger log = LoggerFactory.getLogger(AccountsTransferService.class);
			
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Value("${endpoint.accountBalance}")
	private String retrieveAccountBalanceUrl;
	
	
	public Account retrieveBalances(Long accountId) throws AccountNotExistException{
		Account account = accountsRepository.findByAccountNo(accountId)
		.orElseThrow(() ->  new AccountNotExistException("Account Number:" + accountId + " does not exist.", ErrorCode.ACCOUNT_ERROR));
		
		return account;
	}
	
	@Transactional
	public void transferBalances(TransferRequest transfer,  BigDecimal currencyRate, double chargeFee) throws OverDraftException, AccountNotExistException, SystemException, IOException, JSONException {
		try {
			Account accountFrom = accountsRepository.getAccountForUpdate(transfer.getAccountNumberTransferFrom())
					.orElseThrow(() -> new AccountNotExistException("Account Number: " + transfer.getAccountNumberTransferFrom() + " does not exist.", ErrorCode.ACCOUNT_ERROR));
			
			Account accountTo = accountsRepository.getAccountForUpdate(transfer.getAccountNumberTransferTo())
					.orElseThrow(() -> new AccountNotExistException("Account Number: " + transfer.getAccountNumberTransferTo() + " does not exist.", ErrorCode.ACCOUNT_ERROR));
			
			
			BigDecimal accountBalance = accountFrom.getBalance().subtract(transfer.getAmount());
			BigDecimal accountBalanceRate = accountBalance.subtract(currencyRate);
			double chargeFeeAmt = chargeFee*100;
			BigDecimal totalBalance = accountBalanceRate.subtract(BigDecimal.valueOf(chargeFeeAmt));
			
			if(accountFrom.getBalance().compareTo(totalBalance) < 0) { 
				throw new OverDraftException("Account with id:" + accountFrom.getAccountNo() + " does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR);
			}
			
			log.info("currencyRate: "+currencyRate);
			
			for (int i = 0; i < transfer.getRecurringNumber(); i++) {
				accountFrom.setBalance(totalBalance);
				log.info("Transfer Balance from account number: "+accountFrom.getAccountNo()+ " Account Balance: "+accountFrom.getBalance());
				accountsRepository.save(accountFrom);
				
				accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
				log.info("Transfer Balance to account number: "+accountTo.getAccountNo() + " Account Balance: "+accountTo.getBalance());
				accountsRepository.save(accountTo);
			}
			log.info("Transfer "+ transfer.getAmount() + " AUD "+ " from "+ accountFrom.getAccountName() +" to "+ accountTo.getAccountName());
			log.info("Recurring for " + transfer.getRecurringNumber() +" time(s)"); 
		}catch(Exception e) {
			log.info("Error found: " + e.getMessage()); 
		}
	}
	
	
	public BigDecimal checkAccountBalance(Long accountId) throws SystemException {
		
		try {
			String url = retrieveAccountBalanceUrl.replace("{id}", accountId.toString());
			
			log.info("Checking balance from Internal API Endpoint "+url);
			ResponseEntity<Account> balanceCheckResult = restTemplate.getForEntity(url, Account.class);
			
			if(balanceCheckResult.getStatusCode().is2xxSuccessful()) {
				if(balanceCheckResult.hasBody()) {
				return balanceCheckResult.getBody().getBalance();
				}
			}
		} catch (ResourceAccessException ex) {
			final String errorMessage = "Encounter timeout error, please check with system administrator.";
			
			if(ex.getCause() instanceof SocketTimeoutException) {
				throw new CheckBalanceException(errorMessage, ErrorCode.TIMEOUT_ERROR);
			}
		}
		// for any other fail cases
		throw new SystemException("Encounter internal server error, please check with system administrator.", ErrorCode.SYSTEM_ERROR);
	}
}
