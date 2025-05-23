package com.chase.api.dto;

import java.math.BigDecimal;

public class ExchangeRate {
	private String currencyPair;
	
	private BigDecimal conversationRate;
	
	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public BigDecimal getConversationRate() {
		return conversationRate;
	}

	public void setConversationRate(BigDecimal conversationRate) {
		this.conversationRate = conversationRate;
	}
	
}
