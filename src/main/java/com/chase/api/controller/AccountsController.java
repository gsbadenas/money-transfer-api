package com.chase.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chase.api.account.AccountsTransferService;
import com.chase.api.model.Account;

@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {

	@Autowired
	private AccountsTransferService accountService;

	@GetMapping("/balances/{accountId}")
	public Account getBalance(@PathVariable Long accountId) {
		return accountService.retrieveBalances(accountId);
	}
}
