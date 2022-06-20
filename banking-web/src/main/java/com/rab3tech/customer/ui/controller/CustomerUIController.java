package com.rab3tech.customer.ui.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.SecurityQuestionsVO;
import com.rab3tech.vo.TransactionVO;

/**
 * 
 * 
 * This class for customer GUI
 *
 */
@Controller
public class CustomerUIController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerUIController.class);

	@Autowired
	private CustomerEnquiryService customerEnquiryService;


	@Autowired
	private SecurityQuestionService securityQuestionService;


	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private LoginService loginService;

	@Autowired
	CustomerAccountInfoService customerAccountInfoService; // for task 5

	@Autowired
	PayeeInfoService payeeInfoService;

	@PostMapping("/customer/changePassword")
	public String saveCustomerQuestions(@ModelAttribute ChangePasswordVO changePasswordVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		changePasswordVO.setLoginid(loginid);
		String viewName ="customer/dashboard";
		boolean status=loginService.checkPasswordValid(loginid,changePasswordVO.getCurrentPassword());
		if(status) {
			if(changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				viewName ="customer/dashboard";
				loginService.changePassword(changePasswordVO);
			}else {
				model.addAttribute("error","Sorry , your new password and confirm passwords are not same!");
				return "customer/login";	//login.html	
			}
		}else {
			model.addAttribute("error","Sorry , your username and password are not valid!");
			return "customer/login";	//login.html	
		}
		return viewName;
	}

	@PostMapping("/customer/securityQuestion")
	public String saveCustomerQuestions(@ModelAttribute("customerSecurityQueAnsVO") CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		customerSecurityQueAnsVO.setLoginid(loginid);
		securityQuestionService.save(customerSecurityQueAnsVO);
		//
		return "customer/chagePassword";
	}

	// http://localhost:444/customer/account/registration?cuid=1585a34b5277-dab2-475a-b7b4-042e032e8121603186515
	@GetMapping("/customer/account/registration")
	public String showCustomerRegistrationPage(@RequestParam String cuid, Model model) {

		logger.debug("cuid = " + cuid);
		Optional<CustomerSavingVO> optional = customerEnquiryService.findCustomerEnquiryByUuid(cuid);
		CustomerVO customerVO = new CustomerVO();

		if (!optional.isPresent()) {
			return "customer/error";
		} else {
			// model is used to carry data from controller to the view =- JSP/
			CustomerSavingVO customerSavingVO = optional.get();
			customerVO.setEmail(customerSavingVO.getEmail());
			customerVO.setName(customerSavingVO.getName());
			customerVO.setMobile(customerSavingVO.getMobile());
			customerVO.setAddress(customerSavingVO.getLocation());
			customerVO.setToken(cuid);
			logger.debug(customerSavingVO.toString());
			// model - is hash map which is used to carry data from controller to thyme
			// leaf!!!!!
			// model is similar to request scope in jsp and servlet
			model.addAttribute("customerVO", customerVO);
			return "customer/customerRegistration"; // thyme leaf
		}
	}

	@PostMapping("/customer/account/registration")
	public String createCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		// saving customer into database
		logger.debug(customerVO.toString());
		customerVO = customerService.createAccount(customerVO);
		// Write code to send email

		EmailVO mail = new EmailVO(customerVO.getEmail(), "javahunk2020@gmail.com",
				"Regarding Customer " + customerVO.getName() + "  userid and password", "", customerVO.getName());
		mail.setUsername(customerVO.getUserid());
		mail.setPassword(customerVO.getPassword());
		emailService.sendUsernamePasswordEmail(mail);
		System.out.println(customerVO);
		model.addAttribute("loginVO", new LoginVO());
		model.addAttribute("message", "Your account has been setup successfully , please check your email.");
		return "customer/login";
	}

	@GetMapping(value = { "/customer/account/enquiry", "/", "/mocha", "/welcome" })
	public String showCustomerEnquiryPage(Model model) {
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		// model is map which is used to carry object from controller to view
		model.addAttribute("customerSavingVO", customerSavingVO);
		return "customer/customerEnquiry"; // customerEnquiry.html
	}

	@PostMapping("/customer/account/enquiry")
	public String submitEnquiryData(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		boolean status = customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		logger.info("Executing submitEnquiryData");
		if (status) {
			CustomerSavingVO response = customerEnquiryService.save(customerSavingVO);
			logger.debug("Hey Customer , your enquiry form has been submitted successfully!!! and appref "
					+ response.getAppref());
			model.addAttribute("message",
					"Hey Customer , your enquiry form has been submitted successfully!!! and appref "
							+ response.getAppref());
		} else {
			model.addAttribute("message", "Sorry , this email is already in use " + customerSavingVO.getEmail());
		}
		return "customer/success"; // success.html

	}

	@GetMapping("/customer/myProfile")  // added  getting data from backend database table
	public String myProfilPage(Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			CustomerVO customer=customerService.findCustomer(loginVO2.getUsername());
			model.addAttribute("customerVO",customer);
			List<SecurityQuestionsVO> securityQuestionsVO = securityQuestionService.findAll(); // findAll() method is defined in securityQuestionService class

			model.addAttribute("securityQuestionsVO",securityQuestionsVO );  //it is used  used to make select option in myprofile.html with list of Question
			return "customer/myProfile";

		}
		else {
			return "customer/login";
		}
	}



	@PostMapping("/customer/myProfile")  // added for 2nd task  / Here we save data from outside i.e get from vo and save it to entity
	public String updateMyyProfilePage(@ModelAttribute CustomerVO customerVO ,Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {

			String message =	customerService.updateCustomer(customerVO);
			model.addAttribute("message", message);

			List<SecurityQuestionsVO> securityQuestionsVO = securityQuestionService.findAll(); //it will post questions in html after post
			model.addAttribute("securityQuestionsVO",securityQuestionsVO );  //

			return "customer/myProfile";

		}
		else {
			return "customer/login";
		}
	}


	@GetMapping("customer/addPayee")  //4/08/2021     Task: 5
	public String addPayee(Model model, HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			//check my account is available or not
			boolean accountavailable =customerAccountInfoService.checkValidAccountForCustomer(loginVO2.getUsername()); 

			if(accountavailable) { // if customer has valid account then only show addPayee page
				return "customer/addPayee";
			}
			else {
				model.addAttribute("message","you can not add payee as you don't have valid account");
				return "customer/dashboard";
			}
		}

		else {
			return "customer/login";
		}
	}

	@PostMapping("customer/addPayee")  //4/09/2021     Task: 6
	public String savePayee(Model model, HttpSession session, @ModelAttribute PayeeInfoVO payeeInfoVO) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {

			payeeInfoVO.setCustomerId(loginVO2.getUsername());



			String message = payeeInfoService.savePayeeInfo(payeeInfoVO);

			model.addAttribute("message", message);

			return "customer/addPayee";


		}
		else {

			return "customer/login";
		}

	}


	@GetMapping("customer/makeTransfer")
	public String makeTransferMoney(Model model, HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			List<PayeeInfoVO> payeeVO = payeeInfoService.findAllPayee(loginVO2.getUsername());
			if(payeeVO.size()>0) {
				model.addAttribute("payees",payeeVO);
				return "customer/makeTransfer";
			}
			else {
				model.addAttribute("message","You can not use this feature as you dont have valid payee added");
				return "customer/dashboard";
			}
		}
		else {

			return "customer/login"; 
		}

	}


	@PostMapping("customer/makeTransfer")
	public String saveTransaction(Model model, HttpSession session,@ModelAttribute TransactionVO transactionVO) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			List<PayeeInfoVO> payeeVO = payeeInfoService.findAllPayee(loginVO2.getUsername()); // this is to display payeelist after post
	          if(payeeVO.size()>0) {
				model.addAttribute("payees",payeeVO);
			}

			transactionVO.setCustomerId(loginVO2.getUsername());
		
			String message= customerAccountInfoService.transferAmmount(transactionVO);
			model.addAttribute("message",message);
			return "customer/makeTransfer";
		}
		else {

			return "customer/login"; 
		}

	}



	@GetMapping("customer/getAllPayee") // for payeeList to use api link
	public String getAllPayee(Model model, HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			List<PayeeInfoVO> payeeVO = payeeInfoService.findAllPayee(loginVO2.getUsername());
			if(payeeVO.size()>0) {
				model.addAttribute("payees",payeeVO);
				return "customer/payeeList";
			}
			else {
				model.addAttribute("message","You can not use this feature as you dont have valid payee added");
				return "customer/dashboard";
			}
		}
		else {

			return "customer/login"; 
		}

	}





}







