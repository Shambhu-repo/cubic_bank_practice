package com.rab3tech.customer.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.utils.BankHttpUtils;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;

@Controller
public class EmployeeUIController {
	
    private static final Logger logger = LoggerFactory.getLogger(EmployeeUIController.class);
	
	@Autowired
	private CustomerEnquiryService customerEnquiryService;
	
	
	@Autowired
	private CustomerAccountInfoService customerAccInfoService; // added for 4th task   
	
	
	
	
	@Value("${customer.registration.url}")
	private String registrationURL;
	
	@Autowired
	private EmailService emailService;
	
	
	@GetMapping(value= {"/customer/enquiries"})
    @PreAuthorize("hasAuthority('EMPLOYEE')")
	public String showCustomerEnquiry(Model model) {
		logger.info("showCustomerEnquiry is called!!!");
		List<CustomerSavingVO> pendingApplications = customerEnquiryService.findPendingEnquiry();
		model.addAttribute("applicants", pendingApplications);
		return "employee/customerEnquiryList";	//login.html
	}
	
	@PostMapping("/customers/enquiry/approve")
	public String customerEnquiryApprove(@RequestParam int csaid,HttpServletRequest request) {
		CustomerSavingVO customerSavingVO=customerEnquiryService.changeEnquiryStatus(csaid, "APPROVED");
		String cuuid=BankHttpUtils.generateToken();
		customerEnquiryService.updateEnquiryRegId(csaid, cuuid);
		String registrationLink=BankHttpUtils.getServerBaseURL(request)+"/"+registrationURL+cuuid;
		//String registrationLink ="http://localhost:8080/v3/customer/registration/complete";
		EmailVO mail=new EmailVO(customerSavingVO.getEmail(),"javahunk2020@gmail.com","Regarding Customer "+customerSavingVO.getName()+"  Account registration","",customerSavingVO.getName());
		mail.setRegistrationlink(registrationLink);
		emailService.sendRegistrationEmail(mail);
		return "redirect:/customer/enquiries";
	}
	
	
	/* Shambhu    4/6/2021 */
	
	@GetMapping(value= {"/customer/createAccount"})  
    @PreAuthorize("hasAuthority('EMPLOYEE')")
	public String customerCreateAccount(Model model) {

		List<CustomerVO> customers =customerAccInfoService.getCustomersForAccountCreation();
	      model.addAttribute("customers", customers);

		logger.info("customer/createAccount is called!!!");
	
		
		return "employee/createAccount";	//createAccount.html
	} 
	
	
	/* Shambhu  */
	@PostMapping(value= {"/customer/createAccount"})  
    @PreAuthorize("hasAuthority('EMPLOYEE')")
	public String createAccount(Model model, @RequestParam int customerId) {  // here we are requesting from html form : customerId so it will save
		// that customer only which we want to save by clicking in customer Account link in customerAccount.html  
		
	   // customerAccInfoService.createAccount(customerId);
	    
	  customerAccInfoService.createAccount(customerId);

	
	    // String message= " Account is created ";
	    
	     model.addAttribute("message","Account is created");

		
		return "redirect:/customer/createAccount";	//createAccount.html
	} 
	
	

}
