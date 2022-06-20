package com.rab3tech.vo;

import java.util.Date;

public class TransactionVO {
	
	private String debitorAccountNumber;
	private String creditorAccountNumber;
	private float amount;
	private String remarks;
	private String customerId;
	
	public String getDebitorAccountNumber() {
		return debitorAccountNumber;
	}
	public void setDebitorAccountNumber(String debitorAccountNumber) {
		this.debitorAccountNumber = debitorAccountNumber;
	}
	public String getCreditorAccountNumber() {
		return creditorAccountNumber;
	}
	public void setCreditorAccountNumber(String creditorAccountNumber) {
		this.creditorAccountNumber = creditorAccountNumber;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "TransactionVO [debitorAccountNumber=" + debitorAccountNumber + ", creditorAccountNumber="
				+ creditorAccountNumber + ", amount=" + amount + ", remarks=" + remarks + ", customerId=" + customerId
				+ "]";
	}
	

}
