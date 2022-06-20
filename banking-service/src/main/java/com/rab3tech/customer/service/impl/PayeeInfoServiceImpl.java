package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;

import groovy.lang.BenchmarkInterceptor;


@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService {  // this is for task 6

	@Autowired
	PayeeInfoRepository payeeInfoRepo;

	@Autowired
	LoginRepository loginrepository;

	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepo;

	@Autowired
	CustomerRepository customerRepo;

	@Override
	public String savePayeeInfo(PayeeInfoVO payeeInfoVO) {
		String message=null;		

		Optional<CustomerAccountInfo> customerAccount = customerAccountInfoRepo.findByCustomerId(payeeInfoVO.getCustomerId());  // customerId is user's login id
		if(!customerAccount.isPresent()) {
			message="Customer is not having valid account";
			return message;
		}
		//payee has valid account or not check
		Optional<CustomerAccountInfo> payeeAccount = customerAccountInfoRepo.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());  // customerId is user's login id
		if (!payeeAccount.isPresent()) {
			message="payee is not having valid account";
			return message;
		} 

		//payee name is vlid or not
		Optional<Customer> customer = customerRepo.findByEmail(payeeAccount.get().getCustomerId());
		if(!customer.get().getName().equalsIgnoreCase(payeeInfoVO.getPayeeName())) {
			message = "payee's name is not correct";
			return message;
		}

		//do not add duplicate payee into payee info  //
        Optional<PayeeInfo> duplicatePayee =  payeeInfoRepo.findBycustomerIdAndPayeeAccountNo(payeeInfoVO.getCustomerId(), payeeInfoVO.getPayeeAccountNo());
		if(duplicatePayee.isPresent()) {
			message="Same payee can  not be added again";
			return message;
		}
		PayeeInfo payeeInfo = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payeeInfo);

		payeeInfo.setStatus("Valid");
		payeeInfo.setDoe(new Timestamp(new Date().getTime()));
		payeeInfo.setDom(new Timestamp(new Date().getTime()));
		payeeInfoRepo.save(payeeInfo);
		message="Payee is added to your account";


		return message;

	}



	@Override
	public List<PayeeInfoVO> findAllPayee(String customerId) {   //task 7 th   // used in api
		List<PayeeInfoVO> payeeInfoVO=new ArrayList<PayeeInfoVO>();
		List<PayeeInfo> payeeList =	payeeInfoRepo.findByCustomerId(customerId);

		for(PayeeInfo payee : payeeList) {
			PayeeInfoVO payeeInfo = new PayeeInfoVO();

			BeanUtils.copyProperties(payee, payeeInfo);
			payeeInfoVO.add(payeeInfo);
		}
		return payeeInfoVO;
	}



	@Override
	public PayeeInfoVO findPayeeById(int payeeId) {                 //api used
		Optional<PayeeInfo> payee = payeeInfoRepo.findById(payeeId);
		PayeeInfoVO payeeInfoVo = new PayeeInfoVO();
		BeanUtils.copyProperties(payee.get(), payeeInfoVo);
		return payeeInfoVo;
	}



	@Override
	public String deletePayeeById(int payeeId) {   // api used
		payeeInfoRepo.deleteById(payeeId);
		return "payee deleted successfully";
	}



	@Override
	public String updatePayee(PayeeInfoVO payyeeInfoVO) { // api used
		Optional<PayeeInfo> payee = payeeInfoRepo.findById(payyeeInfoVO.getId());
		BeanUtils.copyProperties(payyeeInfoVO, payee.get());
		payee.get().setDom(new Timestamp(new Date().getTime()));
		payeeInfoRepo.save(payee.get());
		return "Payee updated successfully";
	}






}
