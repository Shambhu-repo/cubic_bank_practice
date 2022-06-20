package com.rab3tech.customer.service;

import java.util.List;
import java.util.Optional;

import com.rab3tech.vo.PayeeInfoVO;



public interface PayeeInfoService {
	
	String savePayeeInfo(PayeeInfoVO payeeInfoVO); // task 6
	
	
	List<PayeeInfoVO> findAllPayee(String customerId); // task 7
	
	PayeeInfoVO  findPayeeById(int payeeId); // this is for api
	
	String  deletePayeeById(int payeeId); // this is for api
	
	String updatePayee(PayeeInfoVO payyeeInfoVO); // for api

	


	
	
	

}
