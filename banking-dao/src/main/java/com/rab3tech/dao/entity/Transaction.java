package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
 /*  This is to track the transaction that customer does */

@Entity
@Table(name="customer_transaction_tbl")
public class Transaction 
{
	private int id;
	private String debitorAccountNumber;
	private String creditorAccountNumber;
	private float amount;
	private Date doe;
	private String remarks;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public Date getDoe() {
		return doe;
	}
	public void setDoe(Date doe) {
		this.doe = doe;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", debitorAccountNumber=" + debitorAccountNumber + ", creditorAccountNumber="
				+ creditorAccountNumber + ", amount=" + amount + ", doe=" + doe + ", remarks=" + remarks + "]";
	}
	

}
