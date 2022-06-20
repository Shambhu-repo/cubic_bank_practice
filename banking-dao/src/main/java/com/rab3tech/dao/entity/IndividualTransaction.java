package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Individual_transaction_tbl")
public class IndividualTransaction {
	private int id;
	private String customerId;
	private float debitAmount;
	private float creditAmount;
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


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public float getDebitAmount() {
		return debitAmount;
	}


	public void setDebitAmount(float debitAmount) {
		this.debitAmount = debitAmount;
	}


	public float getCreditAmount() {
		return creditAmount;
	}


	public void setCreditAmount(float creditAmount) {
		this.creditAmount = creditAmount;
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
		return "IndividualTransaction [id=" + id + ", customerId=" + customerId + ", debitAmount=" + debitAmount
				+ ", creditAmount=" + creditAmount + ", doe=" + doe + ", remarks=" + remarks + "]";
	}


	

}
