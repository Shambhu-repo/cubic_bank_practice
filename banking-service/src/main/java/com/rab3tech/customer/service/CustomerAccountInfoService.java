package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.dao.entity.Login;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.TransactionVO;

public interface CustomerAccountInfoService {
	
	public List<CustomerVO> getCustomersForAccountCreation();  
	
	public void createAccount(int customerId);
	
	boolean checkValidAccountForCustomer(String customerId);  // added for task 5
	
     String transferAmmount(TransactionVO transactionVO);  // added for task 8th
}
