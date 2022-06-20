package com.rab3tech.customer.service;

import com.rab3tech.dao.entity.Login;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
//import com.mvc.learning.service.StudentDto;
import com.rab3tech.vo.CustomerVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);
   
	public CustomerVO findCustomer(String email);  // added 
    
    
   public String updateCustomer(CustomerVO customerVO); // I added for 2nd task  this is original
    



}
