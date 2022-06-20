package com.rab3tech.customer.service.impl;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;


public class PayeeInfoServiceImplTest {


	//	CustomerAccountInfoRepository customerAccountInfoRepository

	@Mock
	CustomerRepository customerRepository;

	@Mock 
	CustomerAccountInfoRepository customerAccountInfoRepository; 

	@Mock
	LoginRepository loginRepository;



	@Mock 
	PayeeInfoRepository payeeInfoRepository;


	@InjectMocks
	PayeeInfoServiceImpl payeeInfoServiceImpl;





	@Before
	public void init() {
		MockitoAnnotations.initMocks(this); //Initializing mocking for each test cases
	}

	// problem  : Same payee can not be added again : in this line got problem
	// savePayeeInfo from payeeInfoserviceImpl.java  
	@Test  // fail  // Error
	public void testSavePayeeInfo() {
		
		CustomerAccountInfo customerAccountInf = new CustomerAccountInfo();
        customerAccountInf.setCustomerId("sam@gmail.com");  // for the customer is having valid account or not
		customerAccountInf.setAccountNumber("100");
		customerAccountInf.setAccountType("Fixed");
		customerAccountInf.setBranch("Kathmandu");
		customerAccountInf.setId(1);
		
     
//		CustomerAccountInfo customerAccountInf1 = new CustomerAccountInfo();
//        customerAccountInf1.setCustomerId("sam@gmail");  // for the payee is having valid account or not
//		customerAccountInf1.setAccountNumber("101");
//		customerAccountInf1.setAccountType("Fixed");
//		customerAccountInf1.setBranch("Kathmandu");
//		customerAccountInf1.setId(2);
//		
     
		
		
		
		PayeeInfoVO payeeInfoVO=new PayeeInfoVO();
		payeeInfoVO.setPayeeName("Ram");
		payeeInfoVO.setPayeeNickName("sam");
		payeeInfoVO.setCustomerId("sam@gmail.com");
		payeeInfoVO.setPayeeAccountNo("101");

//payeeInfoVO.setCustomerId(customerAccountInf.getCustomerId());
// payeeInfoVO.setCustomerId(customerAccountInfo.get().getCustomerId());
		payeeInfoVO.setDom(new Timestamp(new Date().getTime()));
		payeeInfoVO.setRemarks("Water Bill");
		payeeInfoVO.setId(100);



		Customer customer = new Customer();
		customer.setName("Ram");
		customer.setAddress("pokhara");
		customer.setAge(12);
		customer.setEmail("sam@gmail");


		//      PayeeInfo payeeInfo = new PayeeInfo();   // trying to add sam@gamil.com from payeeInfoVO meaning this payee is already in payeeRepository
		//      payeeInfo.setPayeeName("shambhu");       // throws "same payee can not add "
		//      payeeInfo.setCustomerId("sam@gmail.com");
		//      payeeInfo.setPayeeAccountNo("120");;
		//      


		PayeeInfo payeeInfoDB = new PayeeInfo();  // this is database record 
		payeeInfoDB.setPayeeName("sham");      
		payeeInfoDB.setCustomerId("sam@gmail.com");
		payeeInfoDB.setPayeeAccountNo("120");
		payeeInfoDB.setId(2);

		
//		//payee name is vlid or not
//				Optional<Customer> customer = customerRepo.findByEmail(payeeAccount.get().getCustomerId());
//				if(!customer.get().getName().equalsIgnoreCase(payeeInfoVO.getPayeeName())) {
//					message = "payee's name is not correct";
//					return message;
//				}
//		

		when(customerAccountInfoRepository.findByCustomerId(payeeInfoVO.getCustomerId())).thenReturn(Optional.of(customerAccountInf));
		when(customerAccountInfoRepository.findByAccountNumber(payeeInfoVO.getPayeeAccountNo())).thenReturn(Optional.of(customerAccountInf));
		when(customerRepository.findByEmail(customerAccountInf.getCustomerId())).thenReturn(Optional.of(customer));
        when(payeeInfoRepository.findBycustomerIdAndPayeeAccountNo(payeeInfoVO.getCustomerId(), payeeInfoVO.getPayeeAccountNo())).thenReturn(Optional.of(payeeInfoDB));

		PayeeInfo payee = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payee);

		
//		when(payeeInfoRepository.save(isA(PayeeInfo.class)))
//		.thenReturn(payee);

		//String s =   "Same payee can  not be added again";
		String s =   "Payee is added to your account";


		//assertNotNull(s);
		assertEquals(s,payeeInfoServiceImpl.savePayeeInfo(payeeInfoVO));

	}




	@Test  // pass
	public void testFindAllPayeeWhenExist() {
		List<PayeeInfo> payeeInfo = new ArrayList<PayeeInfo>();

		when(payeeInfoRepository.findByCustomerId("sam@gmail.com"))
		.thenReturn(payeeInfo);
		payeeInfoServiceImpl.findAllPayee("sam@gmail.com");
	}

	@Test //pass
	public void testFindAllPayeeWhenNotExist() {
		PayeeInfo payee = new PayeeInfo();
		List<PayeeInfo> payeeInfo = new ArrayList<PayeeInfo>();
		when(payeeInfoRepository.findByCustomerId("sam@gmail.com"))
		.thenReturn(payeeInfo);
		assertFalse(payeeInfo.contains(payee));
	}


	//pass
	@Test (expected=NoSuchElementException.class)
	public void testFindPayeeByIdIfNotExist() {
		when(payeeInfoRepository.findById(100))
		.thenReturn(Optional.empty());
		payeeInfoServiceImpl.findPayeeById(100);


	}

	@Test  // pass
	public void testFindPayeeByIdIfExist() {
		PayeeInfo payee = new PayeeInfo();
		payee.setPayeeName("Shambhu");
		payee.setPayeeNickName("sam");
		payee.setCustomerId("sam@gmail");
		//payee.setDom(new Timestamp(new Date().getTime()));
		payee.setRemarks("Water Bill");

		when(payeeInfoRepository.findById(100))
		.thenReturn(Optional.of(payee));

		PayeeInfoVO payeeInfoVO=payeeInfoServiceImpl.findPayeeById(100);

		assertNotNull(payeeInfoVO);
		assertEquals("Shambhu", payeeInfoVO.getPayeeName());
		assertEquals("sam", payeeInfoVO.getPayeeNickName());
		assertEquals("sam@gmail", payeeInfoVO.getCustomerId());
		assertEquals("Water Bill", payeeInfoVO.getRemarks());
	}
	@Test  //pass
	public void testDeletePayeeById() {
		doNothing().when(payeeInfoRepository).deleteById(isA(Integer.class));
		String s = "payee deleted successfully";
		assertEquals(s, payeeInfoServiceImpl.deletePayeeById(100));
	}

	@Test  //pass
	public void testUpdatePayee() {
		// Date date = new Date();
		PayeeInfoVO payeeInfoVO=new PayeeInfoVO();
		payeeInfoVO.setPayeeName("Shambhu");
		payeeInfoVO.setPayeeNickName("sam");
		payeeInfoVO.setCustomerId("sam@gmail");
		//payeeInfoVO.setDom(new Timestamp(date.getTime()));
		payeeInfoVO.setDom(new Timestamp(new Date().getTime()));
		payeeInfoVO.setRemarks("Water Bill");


		PayeeInfo payee = new PayeeInfo();
		payee.setPayeeName("Shambhu");
		payee.setPayeeNickName("sam");
		payee.setCustomerId("sam@gmail");
		payee.setDom(new Timestamp(new Date().getTime()));
		payee.setRemarks("Water Bill");

		String  s="Payee updated successfully";

		when(payeeInfoRepository.findById(any(Integer.class)))
		.thenReturn(Optional.of(payee));

		assertEquals(s, payeeInfoServiceImpl.updatePayee(payeeInfoVO));
	}

}
