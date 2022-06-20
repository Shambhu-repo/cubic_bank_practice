package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerAccountInfo;

public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Integer>{
	
	Optional<CustomerAccountInfo> findByCustomerId(String customerId); //findByCustomerId is inbuilt method which will finds customer by it customerId
	Optional<CustomerAccountInfo> findByAccountNumber(String accountNumber); //findByCustomerId is inbuilt method which will finds customer by it customerId

}
