package com.chase.api.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
	
	@NotNull
	private Long accountNumberTransferFrom;

	@NotNull
	private Long accountNumberTransferTo;

	@NotNull
	@Min(value = 0, message = "Transfer amount can not be less than zero")
	private BigDecimal amount;
	
	private Long recurringNumber;
	
	private double chargeFee;
	

	public double getChargeFee() {
		return chargeFee;
	}

	public void setChargeFee(double chargeFee) {
		this.chargeFee = chargeFee;
	}

	public Long getRecurringNumber() {
		return recurringNumber;
	}

	public void setRecurringNumber(Long recurringNumber) {
		this.recurringNumber = recurringNumber;
	}

	@JsonCreator
	public TransferRequest(@NotNull @JsonProperty("accountNumberTransferFrom") Long accountNumberTransferFrom,
			@NotNull @JsonProperty("accountNumberTransferTo") Long accountNumberTransferTo,
			@NotNull @Min(value = 0, message = "Transfer amount can not be less than zero") @JsonProperty("amount") BigDecimal amount, long recurringNumber) {
		super();
		this.accountNumberTransferFrom = accountNumberTransferFrom;
		this.accountNumberTransferTo = accountNumberTransferTo;
		this.amount = amount;
		this.recurringNumber=recurringNumber;
	}
	
	@JsonCreator
	public TransferRequest() {
		super();
	}

	public Long getAccountNumberTransferFrom() {
		return accountNumberTransferFrom;
	}

	public void setAccountNumberTransferFrom(Long accountNumberTransferFrom) {
		this.accountNumberTransferFrom = accountNumberTransferFrom;
	}

	public Long getAccountNumberTransferTo() {
		return accountNumberTransferTo;
	}

	public void setAccountNumberTransferTo(Long accountNumberTransferTo) {
		this.accountNumberTransferTo = accountNumberTransferTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumberTransferFrom == null) ? 0 : accountNumberTransferFrom.hashCode());
		result = prime * result + ((accountNumberTransferTo == null) ? 0 : accountNumberTransferTo.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
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
		TransferRequest other = (TransferRequest) obj;
		if (accountNumberTransferFrom == null) {
			if (other.accountNumberTransferFrom != null)
				return false;
		} else if (!accountNumberTransferFrom.equals(other.accountNumberTransferFrom))
			return false;
		if (accountNumberTransferTo == null) {
			if (other.accountNumberTransferTo != null)
				return false;
		} else if (!accountNumberTransferTo.equals(other.accountNumberTransferTo))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		return true;
	}

}
