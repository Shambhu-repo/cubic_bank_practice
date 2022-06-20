package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository  extends JpaRepository<PayeeInfo, Integer>{
	
	 Optional<PayeeInfo> findBycustomerIdAndPayeeAccountNo(String customerId, String payeeAccountNo ); // task 6
	
	 List<PayeeInfo> findByCustomerId(String customerId); // task 7
	 
	 
	 Optional<PayeeInfo> findBypayeeName(String payeeAccountNo); // added 4/13/2021   ,


	 
}
