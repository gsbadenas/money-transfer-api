package com.chase.api;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.chase.api.account.AccountsTransferService;
import com.chase.api.exception.AccountNotExistException;
import com.chase.api.exception.OverDraftException;
import com.chase.api.exception.SystemException;
import com.chase.api.model.Account;
import com.chase.api.model.TransferRequest;
import com.chase.api.repository.AccountsRepository;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

	@Mock
	AccountsRepository accRepo;
	
	@InjectMocks
	AccountsTransferService accService;
	
	@Test
	public void testRetrieveBalance() {
		when(accRepo.findByAccountNo(1L)).thenReturn(Optional.of(new Account(1L, 1001L, BigDecimal.TEN, "Joe", "USD")));
		assertEquals(BigDecimal.TEN, accService.retrieveBalances(1L).getBalance());
	}
	
	@Test
	public void testRetrieveBalanceFromInvalidAccount() {
		when(accRepo.findByAccountNo(1010L)).thenReturn(Optional.empty());
		//assertEquals(BigDecimal.TEN, accService.retrieveBalances(1L).getBalance());
	}
	
	@Test
	public void testTransferBalance() throws Exception, Exception, Exception {
		Long accountFromId = 1001L;
		Long accountFromTo = 2001L;
		BigDecimal amount = new BigDecimal(10);
		
		TransferRequest request = new TransferRequest();
		request.setAccountNumberTransferFrom(accountFromId);
		request.setAccountNumberTransferTo(accountFromTo);
		request.setAmount(amount);
		request.setRecurringNumber(1L);
		Account accFrom = new Account(1L, accountFromId, BigDecimal.TEN,"test", "USD");
		Account accTo = new Account(1L, accountFromTo, BigDecimal.TEN, "test2", "USD");
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		BigDecimal cRate = new BigDecimal(1.1);
		accService.transferBalances(request, cRate, 0.01);
		
		//assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}

	@Test
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException, SystemException, IOException, JSONException {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(20);
		
		TransferRequest request = new TransferRequest();
		request.setAccountNumberTransferFrom(accountFromId);
		request.setAccountNumberTransferTo(accountFromTo);
		request.setAmount(amount);
		request.setRecurringNumber(1L);
		
		Account accFrom = new Account(1L, accountFromId, BigDecimal.TEN,"test", "USD");
		Account accTo = new Account(1L, accountFromTo, BigDecimal.TEN, "test2", "USD");
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		BigDecimal cRate = new BigDecimal(1.1);
		accService.transferBalances(request, cRate, 0.01);
	}
}


