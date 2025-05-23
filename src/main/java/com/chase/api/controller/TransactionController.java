package com.chase.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chase.api.account.AccountsTransferService;
import com.chase.api.constant.ErrorCode;
import com.chase.api.dto.ExchangeRate;
import com.chase.api.dto.TransferResult;
import com.chase.api.exception.AccountNotExistException;
import com.chase.api.exception.CheckBalanceException;
import com.chase.api.exception.OverDraftException;
import com.chase.api.model.Account;
import com.chase.api.model.TransferRequest;
import com.chase.api.repository.AccountsRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private AccountsTransferService accountService;
	
	@Value("${external.api.exchange_rate}")
	private String getExternalApiExchangeRate;
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	
	@PostMapping(consumes = { "application/json" })
	public ResponseEntity transferMoney(@RequestBody @Valid TransferRequest request) throws Exception {
		TransferResult result = new TransferResult();
		try {
		
			Account accountFrom = accountsRepository.getAccountForUpdate(request.getAccountNumberTransferFrom())
					.orElseThrow(() -> new AccountNotExistException("Account Number: " + request.getAccountNumberTransferFrom() + " does not exist.", ErrorCode.ACCOUNT_ERROR));

			Account accountTo = accountsRepository.getAccountForUpdate(request.getAccountNumberTransferTo())
					.orElseThrow(() -> new AccountNotExistException("Account Number: " + request.getAccountNumberTransferTo() + " does not exist.", ErrorCode.ACCOUNT_ERROR));
			
			String currencyRate = getExchangeRateData(accountFrom.getCurrency(), accountTo.getCurrency());
			BigDecimal rate = new BigDecimal(currencyRate);
			accountService.transferBalances(request, rate, request.getChargeFee());
			
			result.setAccountName(accountFrom.getAccountName());
			result.setAccountNumber(request.getAccountNumberTransferFrom());
			result.setBalanceAfterTransfer(accountService.checkAccountBalance(request.getAccountNumberTransferFrom()));
			//result.setMessage("Transfer "+ request.getAmount()+ " USD " + "from "+  Alice to Bob");
			
			result.setMessage("Money transferred complete");
			log.info("Account from:" +result.getAccountNumber());
			log.info("Account Balance after transfer:" +result.getBalanceAfterTransfer());

			
			ExchangeRate exchangeRate = new ExchangeRate();
			exchangeRate.setCurrencyPair(accountFrom.getCurrency()+"/"+accountTo.getCurrency());
			
			exchangeRate.setConversationRate(rate);
			result.setExchangeRate(exchangeRate);
			result.setTransactionPercentageFee(request.getChargeFee()*100+""+"% applied");
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | OverDraftException e) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw new AccountNotExistException("Account number: " + request.getAccountNumberTransferFrom() + " does not exist.", ErrorCode.ACCOUNT_ERROR);
		}catch (CheckBalanceException cbEx) {
			String errorMessage = "Fail to check balances after transfer, please check with system administrator.";
			log.error(errorMessage);
			throw new CheckBalanceException(errorMessage, ErrorCode.TIMEOUT_ERROR);
		}
	}
	
	  private String getExchangeRateData(String currency, String currencyExchangeRate) throws IOException, JSONException {
	        String data = null;
	        StringBuilder responseData = new StringBuilder();
	        JsonObject jsonObject = null;
	        URL url = null;
	        String rate = "";
	        url = new URL(getExternalApiExchangeRate + currency);        
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("User-Agent", "Mozilla/5.0");
	        int responseCode = con.getResponseCode();
	        log.info("Sending 'GET' request to URL Extenal API: Exchange Rate - " + url);
	        // System.out.println("Response Code : " + responseCode);
	        try (BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream()))) {
	            String line;
	            while ((line = in.readLine()) != null) {
	                responseData.append(line);
	            }
	            jsonObject = new Gson().fromJson(responseData.toString(), JsonObject.class);
	            
	            data = jsonObject.get("rates").toString();
	    
	            JSONObject jsonObj = new JSONObject(
	            		data);
	            rate = jsonObj.get(currencyExchangeRate).toString();
	        }
	        return rate;
	    }
	
}
