package com.chase.api.dto;

import java.math.BigDecimal;

public class TransferResult {
	
	private String accountName;
	
	private Long accountNumber;
	
	private BigDecimal balanceAfterTransfer;
	
	private String message;
	
	private ExchangeRate exchangeRate;
	
	private String transactionPercentageFee;
	
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTransactionPercentageFee() {
		return transactionPercentageFee;
	}

	public void setTransactionPercentageFee(String transactionPercentageFee) {
		this.transactionPercentageFee = transactionPercentageFee;
	}

	public ExchangeRate getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(ExchangeRate exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalanceAfterTransfer() {
		return balanceAfterTransfer;
	}

	public void setBalanceAfterTransfer(BigDecimal balanceAfterTransfer) {
		this.balanceAfterTransfer = balanceAfterTransfer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((balanceAfterTransfer == null) ? 0 : balanceAfterTransfer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferResult other = (TransferResult) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (balanceAfterTransfer == null) {
			if (other.balanceAfterTransfer != null)
				return false;
		} else if (!balanceAfterTransfer.equals(other.balanceAfterTransfer))
			return false;
		return true;
	}
	
}
