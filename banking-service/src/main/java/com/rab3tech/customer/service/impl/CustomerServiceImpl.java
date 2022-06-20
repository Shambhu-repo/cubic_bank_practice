package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;

@Service
@Transactional
public class CustomerServiceImpl implements  CustomerService{

	@Autowired
	private MagicCustomerRepository customerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private LoginRepository loginrepository;  // i have added it for 2nd task

	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionAnswer;
	
	@Autowired
	SecurityQuestionsRepository securityQuestionsRepository;





	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword=PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");

		Role entity=roleRepository.findById(3).get();
		Set<Role> roles=new HashSet<>();
		roles.add(entity);
		//setting roles inside login
		login.setRoles(roles);
		//setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer=customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());
		return customerVO;
	}


	@Override
	public CustomerVO findCustomer(String email) { // this is for getting the customer update profile and fill it up  : is used in getMapping
		CustomerVO customerVO=new CustomerVO();
		Optional<Customer> customer=customerRepository.findByEmail(email);
		if(customer.isPresent()) {
			BeanUtils.copyProperties(customer.get(), customerVO);
		}
		//get Q\a and pass it in vo
		List<CustomerQuestionAnswer>  questionAnswers  =  customerQuestionAnswer.findQuestionAnswer(email);

		customerVO.setQuestion1(questionAnswers.get(0).getQuestion()); // get(0) will give first question
		customerVO.setQuestion2(questionAnswers.get(1).getQuestion()); // get(1) will give second question
		customerVO.setAnswer1(questionAnswers.get(0).getAnswer()); // get(0) will give first Answer
		customerVO.setAnswer2(questionAnswers.get(1).getAnswer()); // get(1) will give second anser

		return customerVO;
	}

	@Override 
	public String updateCustomer(CustomerVO customerVO) { //is used to post
		Optional  <Login> login = loginrepository.findByLoginid(customerVO.getEmail()); // it will give Login Email


		Customer customer = new Customer();
		BeanUtils.copyProperties(customerVO, customer);
		customer.setLogin(login.get());
		customer.setDom(new Timestamp(new Date().getTime()));
		customerRepository.save(customer);  // this will update customer table

        List<CustomerQuestionAnswer>  cusQA  =  customerQuestionAnswer.findQuestionAnswer(customer.getEmail());
        
        
          // update 1st question and Answer
		cusQA.get(0).setAnswer(customerVO.getAnswer1());
		Optional<SecurityQuestions> question1 = securityQuestionsRepository.findById(Integer.parseInt(customerVO.getQuestion1()));
		cusQA.get(0).setQuestion(question1.get().getQuestions());
		cusQA.get(0).setDom(new Timestamp(new Date().getTime()));
		
		//update 2nd question And Answer
		cusQA.get(1).setAnswer(customerVO.getAnswer2());
	    Optional<SecurityQuestions> question2 = securityQuestionsRepository.findById(Integer.parseInt(customerVO.getQuestion2())); // it will get list of
	    // questions from database . This is repository for securityquestions .//
		cusQA.get(1).setQuestion(question2.get().getQuestions());                                                                  
		cusQA.get(1).setDom(new Timestamp(new Date().getTime()));

        customerQuestionAnswer.saveAll(cusQA);           // this will update customer_question_answer_tbl with both Question and Answer


		String message =" Customer Profile is updated ";


		return message;
	}




}
