package com.rab3tech.customer.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.SecurityQuestionsVO;


public class CustomerUIControllerTest {
	
	@Autowired 
	private MockMvc mockMvc;
	
	@Mock
private CustomerEnquiryService customerEnquiryService;


	@Mock
	private SecurityQuestionService securityQuestionService;


	@Mock

	private CustomerService customerService;

	@Mock

	private EmailService emailService;

	@Mock


	private LoginService loginService;

	@Mock


	CustomerAccountInfoService customerAccountInfoService; 

	@Mock

	PayeeInfoService payeeInfoService;
	
	@InjectMocks
	CustomerUIController customerUIController;
	
	@Before 
	public void initMocks() {
	    MockitoAnnotations.initMocks(this);
	}
	
	
//	@GetMapping("/customer/myProfile")  // added  getting data from backend database table
//	public String myProfilPage(Model model,HttpSession session) {
//		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
//		if(loginVO2!=null) {
//			CustomerVO customer=customerService.findCustomer(loginVO2.getUsername());
//			model.addAttribute("customerVO",customer);
//			List<SecurityQuestionsVO> securityQuestionsVO = securityQuestionService.findAll(); // findAll() method is defined in securityQuestionService class
//
//			model.addAttribute("securityQuestionsVO",securityQuestionsVO );  //it is used  used to make select option in myprofile.html with list of Question
//			return "customer/myProfile";
//
//		}
//		else {
//			return "customer/login";
//		}
	
//}	

	@Test
	public void myProfilePageTest() {
		
		
		CustomerVO customer = new CustomerVO();
		customer.setName("shuravi");
		customer.setAddress("Las Angels");
		customer.setEmail("sha@gmail.com");
		
		
		LoginVO loginVO = new LoginVO();
		loginVO.setUsername("shuravi");
		loginVO.setEmail("sha@gmail.com");
		
		
		
		List<SecurityQuestionsVO> securityquestionVO = new ArrayList<SecurityQuestionsVO>();
		
		SecurityQuestionsVO securityQuestionVO = new SecurityQuestionsVO();
		securityQuestionVO.setQuestions("what is your name");
		
		SecurityQuestionsVO securityQuestionVO1 = new SecurityQuestionsVO();
        securityQuestionVO1.setQuestions("what is your birthplae");

		
		securityquestionVO.add(securityQuestionVO);
		securityquestionVO.add(securityQuestionVO1);

	   
		Mockito.when(securityQuestionService.findAll()).thenReturn(securityquestionVO);
		Mockito.when(customerService.findCustomer(loginVO.getUsername())).thenReturn(customer);
		
		
		
	}

}
